package pl.pgorny.fixerioapidisplay.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.pgorny.fixerioapidisplay.data.model.RateRow

class RateRowViewModel(rateRow: RateRow) : ViewModel() {

    val currencyCode = MutableLiveData(rateRow.currencyCode)
    val rate = MutableLiveData(rateRow.rate.toString())

    fun openDetails() {

    }
}