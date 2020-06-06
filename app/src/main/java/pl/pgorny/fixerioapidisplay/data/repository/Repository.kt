package pl.pgorny.fixerioapidisplay.data.repository

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import pl.pgorny.fixerioapidisplay.data.dto.FixerIoApiFailureResponse
import pl.pgorny.fixerioapidisplay.data.dto.FixerIoApiResponse
import pl.pgorny.fixerioapidisplay.data.dto.FixerIoApiSuccessResponse
import pl.pgorny.fixerioapidisplay.data.model.DateRow
import pl.pgorny.fixerioapidisplay.data.model.RateRow
import pl.pgorny.fixerioapidisplay.data.model.Row
import pl.pgorny.fixerioapidisplay.util.Event
import pl.pgorny.fixerioapidisplay.util.ShowApiErrorEvent
import pl.pgorny.fixerioapidisplay.util.SingleLiveEvent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import kotlin.random.Random

class Repository(private val apiAccessKey: String, private val eventLiveData: SingleLiveEvent<Event>) : PageKeyedDataSource<DateTime, Row>() {
    private val fixerIoApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://data.fixer.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FixerIoApi::class.java)
    }

    private fun handleSuccessResponse(response: FixerIoApiSuccessResponse) : List<Row> {
        return mutableListOf<Row>(DateRow(response.date)).also { list ->
            list.addAll(response.rates.map {
                RateRow(it.key, it.value)
            })
        }
    }

    private fun getNextKey(date: DateTime) : DateTime = date.minusDays(1)

    private fun mock(date: String) : Response<FixerIoApiResponse> {
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

    private suspend fun makeApiCall(date: DateTime, pageSize: Int) : Pair<List<Row>, DateTime?> {
        val result = mutableListOf<Row>()
        var nextKey: DateTime? = date
        try {
            for (i in 0 until pageSize) {
                nextKey?.let {
//                    val response = fixerIoApi.getHistoricalDataForDate(it.toFixerIoDateQueryFormat(), apiAccessKey)
                    val response = mock(it.toFixerIoDateQueryFormat())//TODO replace with call to server
                    if(response.isSuccessful){
                        val fixerIoResponse = response.body()?.getResponse() ?: throw Exception("Api returned empty body")
                        when(fixerIoResponse){
                            is FixerIoApiSuccessResponse -> {
                                val rows = handleSuccessResponse(fixerIoResponse)
                                nextKey = getNextKey(it)
                                result.addAll(rows)
                            }
                            is FixerIoApiFailureResponse -> {
                                throw Exception(fixerIoResponse.error.info)
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
                ShowApiErrorEvent(
                    "Api call was not successful"
                )
            )
            nextKey = null
        }
        return Pair(result, nextKey)
    }

    override fun loadInitial(
        params: LoadInitialParams<DateTime>,
        callback: LoadInitialCallback<DateTime, Row>
    ) {
        Timber.e("Load initial")
        GlobalScope.launch {
            val listWithNextPageKey = makeApiCall(DateTime(), params.requestedLoadSize)
            Timber.e("Load initial: ${listWithNextPageKey.first.size}")
            Timber.e("Load initial-last element: ${listWithNextPageKey.first.lastOrNull()}")
            callback.onResult(listWithNextPageKey.first, null, listWithNextPageKey.second)
        }
    }

    override fun loadAfter(params: LoadParams<DateTime>, callback: LoadCallback<DateTime, Row>) {
        Timber.e("Load after")
        GlobalScope.launch {
            val listWithNextPageKey = makeApiCall(params.key, params.requestedLoadSize)
            Timber.e("Load after: ${listWithNextPageKey.first.size}")
            callback.onResult(listWithNextPageKey.first, listWithNextPageKey.second)
        }
    }

    override fun loadBefore(params: LoadParams<DateTime>, callback: LoadCallback<DateTime, Row>) {
        Timber.e("Load before")
    }

    class Factory(
        private val apiAccessKey: String,
        private val eventLiveData: SingleLiveEvent<Event>
    ) : DataSource.Factory<DateTime, Row>() {
        override fun create(): DataSource<DateTime, Row> {
            return Repository(apiAccessKey, eventLiveData)
        }
    }
}