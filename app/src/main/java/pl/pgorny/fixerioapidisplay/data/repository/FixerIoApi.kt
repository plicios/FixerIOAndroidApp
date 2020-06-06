package pl.pgorny.fixerioapidisplay.data.repository

import org.joda.time.DateTime
import pl.pgorny.fixerioapidisplay.data.dto.FixerIoApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FixerIoApi {
    @GET("{date}")
    suspend fun getHistoricalDataForDate(
        @Path("date") date: String,
        @Query("access_key") accessKey: String,
        @Query("symbols") symbols: String = "USD,AUD,CAD,PLN,MXN,EUR,RUB"
    ) : Response<FixerIoApiResponse>
}

fun DateTime.toFixerIoDateQueryFormat(): String = this.toString("yyyy-MM-dd")