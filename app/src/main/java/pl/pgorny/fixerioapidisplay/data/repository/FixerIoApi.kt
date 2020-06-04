package pl.pgorny.fixerioapidisplay.data.repository

import org.joda.time.DateTime
import pl.pgorny.fixerioapidisplay.data.dto.FixerIoApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

abstract class FixerIoApi {
    @GET("{date}")
    abstract suspend fun getHistoricalDataForDate(@Path("date") date: String, @Query("access_key") accessKey: String) : Response<FixerIoApiResponse>
}

fun DateTime.toFixerIoDateQueryFormat() = this.toString("yyyy-MM-dd")