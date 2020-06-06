package pl.pgorny.fixerioapidisplay.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.pgorny.fixerioapidisplay.data.model.DateRow
import pl.pgorny.fixerioapidisplay.data.model.RateRow

class DateRowViewModel(dateRow: DateRow) : ViewModel() {
    val date = MutableLiveData(dateRow.date)

    class Factory(private val dateRow: DateRow) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DateRowViewModel(dateRow) as T
        }
    }
}