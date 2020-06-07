package pl.pgorny.fixerioapidisplay.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import pl.pgorny.fixerioapidisplay.R
import pl.pgorny.fixerioapidisplay.data.model.Date
import pl.pgorny.fixerioapidisplay.data.model.Rate
import pl.pgorny.fixerioapidisplay.data.model.ListItem
import pl.pgorny.fixerioapidisplay.databinding.ItemDateBinding
import pl.pgorny.fixerioapidisplay.databinding.ItemRateBinding
import pl.pgorny.fixerioapidisplay.ui.viewModel.DateViewModel
import pl.pgorny.fixerioapidisplay.ui.viewModel.RateViewModel
import pl.pgorny.fixerioapidisplay.util.Event
import pl.pgorny.fixerioapidisplay.util.SingleLiveEvent

class RatesAndDatesAdapter(private val eventLiveData: SingleLiveEvent<Event>) : PagedListAdapter<ListItem, RatesAndDatesAdapter.RowViewHolder>(ListItem.diffUtilCallback) {

    enum class ViewType(val value: Int){
        Rate(1),
        Date(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType){
            ViewType.Date.value -> DateViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_date, parent, false
            ))
            ViewType.Rate.value -> RateViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_rate, parent, false
            ))
            else -> throw IllegalStateException("Unsupported view type")
        }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        when(holder){
            is RateViewHolder ->
                holder.binding.viewModel = RateViewModel(getItem(position) as Rate).also {
                    it.eventLiveData = eventLiveData
                }
            is DateViewHolder ->
                holder.binding.viewModel = DateViewModel(getItem(position) as Date)
            else -> throw IllegalStateException("Unsupported viewHolder type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is Rate ->
                ViewType.Rate.value
            is Date ->
                ViewType.Date.value
            else -> throw IllegalStateException("Unsupported row type")
        }
    }

    abstract class RowViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    class RateViewHolder(val binding: ItemRateBinding) : RowViewHolder(binding)
    class DateViewHolder(val binding: ItemDateBinding) : RowViewHolder(binding)
}