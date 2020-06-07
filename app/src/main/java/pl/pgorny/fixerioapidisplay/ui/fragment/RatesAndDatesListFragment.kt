package pl.pgorny.fixerioapidisplay.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import pl.pgorny.fixerioapidisplay.R
import pl.pgorny.fixerioapidisplay.databinding.FragmentRatesAndDatesListBinding
import pl.pgorny.fixerioapidisplay.ui.*
import pl.pgorny.fixerioapidisplay.ui.adapters.RatesAndDatesAdapter
import pl.pgorny.fixerioapidisplay.ui.viewModel.RatesAndDatesListViewModel

class RatesAndDatesListFragment : Fragment() {
    private val eventLiveData =
        MutableLiveData<Event>()

    private val viewModel by viewModels<RatesAndDatesListViewModel>(factoryProducer = {
        RatesAndDatesListViewModel.Factory(
            getString(R.string.api_access_key),
            eventLiveData
        )
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRatesAndDatesListBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_rates_and_dates_list, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.eventLiveData = eventLiveData

        eventLiveData.observe(viewLifecycleOwner) {
            handleEvent(it)
        }

        val adapter = RatesAndDatesAdapter(eventLiveData)
        binding.rowsListFragmentRowsList.adapter = adapter
        viewModel.rows.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return binding.root
    }

    private fun handleEvent(event: Event){
        when(event){
            is ShowRateDetails ->
                findNavController().navigate(
                    R.id.action_rates_list_dest_to_rate_fragment_dest,
                    bundleOf(
                        "rate" to event.rate
                    )
                )
            is ShowError ->
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.error_dialog_title)
                    .setMessage(event.errorText)
                    .setNeutralButton(R.string.error_dialog_button_title) {
                            dialog, _ ->
                        viewModel.invalidateDataSource()
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            is Loading ->
                viewModel.progressBarVisible.postValue(true)
            is FinishedLoading ->
                viewModel.progressBarVisible.postValue(false)
            is NoMoreData ->
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.no_more_data_dialog_title)
                    .setMessage(R.string.no_more_data_dialog_message)
                    .setNeutralButton(R.string.no_more_data_dialog_button_title) {
                            dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()

        }
    }
}