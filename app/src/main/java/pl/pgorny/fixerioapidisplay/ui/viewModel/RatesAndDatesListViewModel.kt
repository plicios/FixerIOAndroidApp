package pl.pgorny.fixerioapidisplay.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import pl.pgorny.fixerioapidisplay.data.model.ListItem
import pl.pgorny.fixerioapidisplay.data.repository.RatesAndDatesDataSource
import pl.pgorny.fixerioapidisplay.util.Event
import pl.pgorny.fixerioapidisplay.util.SingleLiveEvent

class RatesAndDatesListViewModel(apiKey: String, eventLiveData: SingleLiveEvent<Event>) : ViewModel() {
    val progressBarVisible = MutableLiveData(false)

    private val config = PagedList.Config.Builder()
        .setPageSize(1)
        .setInitialLoadSizeHint(3)
        .setPrefetchDistance(2)
        .build()


    private val dataSourceFactory = RatesAndDatesDataSource.Factory(apiKey, eventLiveData)
    val rows: LiveData<PagedList<ListItem>> = LivePagedListBuilder(dataSourceFactory, config).build()

    var eventLiveData = eventLiveData
        set(value){
            field = value
            val dataSource = rows.value?.dataSource
            if(dataSource is RatesAndDatesDataSource){
                dataSource.eventLiveData = eventLiveData
            }
        }

    fun invalidateDataSource(){
        rows.value?.dataSource?.invalidate()
    }

    class Factory(
        private val apiKey: String,
        private val eventLiveData: SingleLiveEvent<Event>
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RatesAndDatesListViewModel(apiKey, eventLiveData) as T
        }
    }
}