package pl.pgorny.fixerioapidisplay.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.pgorny.fixerioapidisplay.data.model.DateRow

class DateRowViewModel(dateRow: DateRow) : ViewModel() {
    val date = MutableLiveData(dateRow.date)
}