package pl.pgorny.fixerioapidisplay.data.dto

data class FixerIoApiResponse (
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)