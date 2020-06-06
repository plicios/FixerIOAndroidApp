package pl.pgorny.fixerioapidisplay.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RateRow(val date: String, val currencyCode: String, val rate: Double) : Row(), Parcelable