package pl.pgorny.fixerioapidisplay.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import pl.pgorny.fixerioapidisplay.data.dto.FixerIoApiFailureResponse
import pl.pgorny.fixerioapidisplay.data.dto.FixerIoApiResponse
import pl.pgorny.fixerioapidisplay.data.dto.FixerIoApiSuccessResponse
import pl.pgorny.fixerioapidisplay.data.model.Date
import pl.pgorny.fixerioapidisplay.data.model.Rate
import pl.pgorny.fixerioapidisplay.data.model.ListItem
import pl.pgorny.fixerioapidisplay.ui.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import kotlin.random.Random

class RatesAndDatesDataSource(private val apiAccessKey: String, var eventLiveData: MutableLiveData<Event>, private val useMockInsteadOfApi: Boolean = false) : PageKeyedDataSource<DateTime, ListItem>() {
    private val fixerIoApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://data.fixer.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FixerIoApi::class.java)
    }

    private fun handleSuccessResponse(response: FixerIoApiSuccessResponse) : List<ListItem> {
        return mutableListOf<ListItem>(Date(response.date)).also { list ->
            list.addAll(response.rates.map {
                Rate(response.date, it.key, it.value)
            })
        }
    }

    private fun getNextKey(date: DateTime) : DateTime = date.minusDays(1)

    private suspend fun mock(date: String) : Response<FixerIoApiResponse> {
        delay( 1000)
        return Response.success(
            FixerIoApiResponse(
                true,
                10,
                "EUR",
                date,
                mapOf(
                    "USD" to Random(date.hashCode()).nextDouble(),
                    "AUD" to Random(date.hashCode()).nextDouble(),
                    "CAD" to Random(date.hashCode()).nextDouble(),
                    "PLN" to Random(date.hashCode()).nextDouble(),
                    "MXN" to Random(date.hashCode()).nextDouble(),
                    "EUR" to 1.0,
                    "RUB" to Random(date.hashCode()).nextDouble()
                ),
                null
            )
        )
    }

    private suspend fun makeApiCall(date: DateTime, pageSize: Int) : Pair<List<ListItem>, DateTime?> {
        val result = mutableListOf<ListItem>()
        var nextKey: DateTime? = date
        try {
            for (i in 0 until pageSize) {
                nextKey?.let {
                    val response = if(useMockInsteadOfApi)
                        mock(it.toFixerIoDateQueryFormat())
                    else
                        fixerIoApi.getHistoricalDataForDate(it.toFixerIoDateQueryFormat(), apiAccessKey)

                    if(response.isSuccessful){
                        val fixerIoResponse = response.body()?.getResponse() ?: throw Exception("Api returned empty body")
                        when(fixerIoResponse){
                            is FixerIoApiSuccessResponse -> {
                                val rows = handleSuccessResponse(fixerIoResponse)
                                nextKey = getNextKey(it)
                                result.addAll(rows)
                            }
                            is FixerIoApiFailureResponse -> {
                                when(fixerIoResponse.error.code){
                                    106 -> {
                                        //No more data to fetch
                                        nextKey = null
                                        eventLiveData.postValue(NoMoreData())
                                    }
                                    else ->
                                        throw Exception(fixerIoResponse.error.info)
                                }


                            }
                            else -> throw IllegalStateException("Response must be success or failure")
                        }
                    } else {
                        throw Exception("Api call was not successful: ${response.errorBody()}")
                    }
                } ?: throw Exception("Cannot query Api with null date")
            }
        } catch (e: Exception) {
            Timber.e(e)
            eventLiveData.postValue(
                ShowError(
                    "Api call was not successful"
                )
            )
            nextKey = null
        }
        return Pair(result, nextKey)
    }

    override fun loadInitial(
        params: LoadInitialParams<DateTime>,
        callback: LoadInitialCallback<DateTime, ListItem>
    ) {
        eventLiveData.postValue(Loading())
        GlobalScope.launch {
            val listWithNextPageKey = makeApiCall(DateTime(), params.requestedLoadSize)
            callback.onResult(listWithNextPageKey.first, null, listWithNextPageKey.second)
            eventLiveData.postValue(FinishedLoading())
        }
    }

    override fun loadAfter(params: LoadParams<DateTime>, callback: LoadCallback<DateTime, ListItem>) {
        eventLiveData.postValue(Loading())
        GlobalScope.launch {
            val listWithNextPageKey = makeApiCall(params.key, params.requestedLoadSize)
            callback.onResult(listWithNextPageKey.first, listWithNextPageKey.second)
            eventLiveData.postValue(FinishedLoading())
        }
    }

    override fun loadBefore(params: LoadParams<DateTime>, callback: LoadCallback<DateTime, ListItem>) {
    }

    class Factory(
        private val apiAccessKey: String,
        private val eventLiveData: MutableLiveData<Event>
    ) : DataSource.Factory<DateTime, ListItem>() {
        override fun create(): DataSource<DateTime, ListItem> {
            return RatesAndDatesDataSource(apiAccessKey, eventLiveData)
        }
    }
}