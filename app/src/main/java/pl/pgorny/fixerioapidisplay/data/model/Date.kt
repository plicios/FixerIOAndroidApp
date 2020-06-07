package pl.pgorny.fixerioapidisplay.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Date(val date: String) : ListItem(), Parcelable