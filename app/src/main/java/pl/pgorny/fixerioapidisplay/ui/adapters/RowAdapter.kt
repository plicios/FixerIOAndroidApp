package pl.pgorny.fixerioapidisplay.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import pl.pgorny.fixerioapidisplay.R
import pl.pgorny.fixerioapidisplay.data.model.DateRow
import pl.pgorny.fixerioapidisplay.data.model.RateRow
import pl.pgorny.fixerioapidisplay.data.model.Row
import pl.pgorny.fixerioapidisplay.databinding.ItemDateRowBinding
import pl.pgorny.fixerioapidisplay.databinding.ItemRateRowBinding
import pl.pgorny.fixerioapidisplay.ui.viewModel.DateRowViewModel
import pl.pgorny.fixerioapidisplay.ui.viewModel.RateRowViewModel
import pl.pgorny.fixerioapidisplay.util.Event
import pl.pgorny.fixerioapidisplay.util.SingleLiveEvent
import timber.log.Timber

class RowAdapter(private val eventLiveData: SingleLiveEvent<Event>) : PagedListAdapter<Row, RowAdapter.RowViewHolder>(Row.diffUtilCallback) {

    enum class ViewType(val value: Int){
        Rate(1),
        Date(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType){
            ViewType.Date.value -> DateRowViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_date_row, parent, false
            ))
            ViewType.Rate.value -> RateRowViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rate_row, parent, false
            ))
            else -> throw IllegalStateException("Unsupported view type")
        }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        when(holder){
            is RateRowViewHolder ->
                holder.binding.viewModel = RateRowViewModel(getItem(position) as RateRow).also {
                    it.eventLiveData = eventLiveData
                }
            is DateRowViewHolder ->
                holder.binding.viewModel = DateRowViewModel(getItem(position) as DateRow)
            else -> throw IllegalStateException("Unsupported viewHolder type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is RateRow ->
                ViewType.Rate.value
            is DateRow ->
                ViewType.Date.value
            else -> throw IllegalStateException("Unsupported row type")
        }
    }

    abstract class RowViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    class RateRowViewHolder(val binding: ItemRateRowBinding) : RowViewHolder(binding)
    class DateRowViewHolder(val binding: ItemDateRowBinding) : RowViewHolder(binding)
}