package pl.pgorny.fixerioapidisplay.data.dto

data class FixerIoApiResponse (
    val success: Boolean,
    val timestamp: Long?,
    val base: String?,
    val date: String?,
    val rates: Map<String, Double>?,
    val error: ErrorDto?
) {
    fun getResponse() : Response {
        return if(success){
            FixerIoApiSuccessResponse(
                timestamp!!,
                base!!,
                date!!,
                rates!!
            )
        } else {
            FixerIoApiFailureResponse(
                error!!
            )
        }
    }
}

abstract class Response

data class FixerIoApiSuccessResponse (
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
) : Response()

data class FixerIoApiFailureResponse (
    val error: ErrorDto
) : Response()