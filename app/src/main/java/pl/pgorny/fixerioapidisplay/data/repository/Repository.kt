package pl.pgorny.fixerioapidisplay.data.repository

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import pl.pgorny.fixerioapidisplay.data.dto.FixerIoApiResponse
import pl.pgorny.fixerioapidisplay.data.model.DateRow
import pl.pgorny.fixerioapidisplay.data.model.RateRow
import pl.pgorny.fixerioapidisplay.data.model.Row
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class Repository(val apiAccessKey: String) : PageKeyedDataSource<DateTime, Row>() {
    private val fixerIoApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://data.fixer.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FixerIoApi::class.java)
    }

    private fun handleResponse(response: FixerIoApiResponse) : List<Row> {
        return mutableListOf<Row>(DateRow()).also { list ->
            list.addAll(response.rates.map {
                RateRow(it.key, it.value)
            })
        }
    }

    fun formatDate(date: DateTime) : String = date.toString("yyyy-MM-dd")
    fun getNextKey(date: DateTime) : DateTime = date.minusDays(1)

    private suspend fun makeApiCall(date: DateTime) : Pair<List<Row>, DateTime?> {
        try {
            val response = fixerIoApi.getHistoricalDataForDate(formatDate(date), apiAccessKey)
            return if(response.isSuccessful){
                val exercisesList = response.body()?.let { handleResponse(it) } ?: listOf()
                val nextKey = getNextKey(date)
                Pair(exercisesList, nextKey)
            } else {
                throw Exception("Api call was not successful: ${response.errorBody()}")
            }
        } catch (e: Exception) {
            Timber.e(e)
//            eventLiveData.postValue(
//                ShowApiErrorEvent(
//                    "Api call was not successful"
//                )
//            )
            return Pair(listOf(), null)
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<DateTime>,
        callback: LoadInitialCallback<DateTime, Row>
    ) {
        GlobalScope.launch {
            val listWithNextPageKey = makeApiCall(DateTime())
            callback.onResult(listWithNextPageKey.first, null, listWithNextPageKey.second)
        }
    }

    override fun loadAfter(params: LoadParams<DateTime>, callback: LoadCallback<DateTime, Row>) {
        GlobalScope.launch {
            val listWithNextPageKey = makeApiCall(params.key)
            callback.onResult(listWithNextPageKey.first, listWithNextPageKey.second)
        }
    }

    override fun loadBefore(params: LoadParams<DateTime>, callback: LoadCallback<DateTime, Row>) {

    }
}