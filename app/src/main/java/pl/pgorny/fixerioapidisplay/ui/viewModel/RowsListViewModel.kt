package pl.pgorny.fixerioapidisplay.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import pl.pgorny.fixerioapidisplay.data.model.Row
import pl.pgorny.fixerioapidisplay.data.repository.Repository
import pl.pgorny.fixerioapidisplay.util.Event
import pl.pgorny.fixerioapidisplay.util.SingleLiveEvent
import timber.log.Timber

class RowsListViewModel(apiKey: String, eventLiveData: SingleLiveEvent<Event>) : ViewModel() {
    val progressBarVisible = MutableLiveData(false)

    private val config = PagedList.Config.Builder()
        .setPageSize(1)
        .setInitialLoadSizeHint(3)
        .setPrefetchDistance(2)
        .build()
    private val dataSourceFactory = Repository.Factory(apiKey, eventLiveData)
    val rows: LiveData<PagedList<Row>> = LivePagedListBuilder(dataSourceFactory, config).setBoundaryCallback(
        object: PagedList.BoundaryCallback<Row>(){
            override fun onZeroItemsLoaded() {
                super.onZeroItemsLoaded()
                Timber.e("onZeroItemsLoaded")
            }

            override fun onItemAtEndLoaded(itemAtEnd: Row) {
                super.onItemAtEndLoaded(itemAtEnd)
                Timber.e("onItemAtEndLoaded: $itemAtEnd")
            }

            override fun onItemAtFrontLoaded(itemAtFront: Row) {
                super.onItemAtFrontLoaded(itemAtFront)
                Timber.e("onItemAtFrontLoaded: $itemAtFront")
            }
        }
    ).build()

    fun invalidateDataSource(){
        rows.value?.dataSource?.invalidate()
    }

    class Factory(
        private val apiKey: String,
        private val eventLiveData: SingleLiveEvent<Event>
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RowsListViewModel(apiKey, eventLiveData) as T
        }
    }
}