package pl.pgorny.fixerioapidisplay.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.pgorny.fixerioapidisplay.data.model.RateRow
import pl.pgorny.fixerioapidisplay.util.Event
import pl.pgorny.fixerioapidisplay.util.ShowRateDetailsEvent
import pl.pgorny.fixerioapidisplay.util.SingleLiveEvent

class RateRowViewModel(private val rateRow: RateRow) : ViewModel() {
    var eventLiveData: SingleLiveEvent<Event>? = null

    val currencyCode = MutableLiveData(rateRow.currencyCode)
    val rate = MutableLiveData(rateRow.rate.toString())

    fun openDetails() {
        eventLiveData?.postValue(ShowRateDetailsEvent(rateRow))
    }

    class Factory(private val rateRow: RateRow) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RateRowViewModel(rateRow) as T
        }
    }
}