package pl.pgorny.fixerioapidisplay.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.pgorny.fixerioapidisplay.data.model.Rate
import pl.pgorny.fixerioapidisplay.util.Event
import pl.pgorny.fixerioapidisplay.util.ShowRateDetails
import pl.pgorny.fixerioapidisplay.util.SingleLiveEvent

class RateViewModel(private val rate: Rate) : ViewModel() {
    var eventLiveData: SingleLiveEvent<Event>? = null

    val date = MutableLiveData(rate.date)
    val currencyCode: MutableLiveData<String> = MutableLiveData(rate.currencyCode)
    val rateValue = MutableLiveData(rate.rate.toString())

    fun openDetails() {
        eventLiveData?.postValue(ShowRateDetails(rate))
    }

    class Factory(private val rate: Rate) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RateViewModel(rate) as T
        }
    }
}