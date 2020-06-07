package pl.pgorny.fixerioapidisplay.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rate(val date: String, val currencyCode: String, val rate: Double) : ListItem(), Parcelable
{

}