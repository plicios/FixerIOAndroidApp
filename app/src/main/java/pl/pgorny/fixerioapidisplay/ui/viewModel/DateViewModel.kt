package pl.pgorny.fixerioapidisplay.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.pgorny.fixerioapidisplay.data.model.Date

class DateViewModel(date: Date) : ViewModel() {
    val date = MutableLiveData(date.date)
}