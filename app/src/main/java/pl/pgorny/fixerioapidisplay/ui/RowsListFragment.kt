package pl.pgorny.fixerioapidisplay.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import pl.pgorny.fixerioapidisplay.R
import pl.pgorny.fixerioapidisplay.databinding.FragmentRowsListBinding
import pl.pgorny.fixerioapidisplay.ui.adapters.RowAdapter
import pl.pgorny.fixerioapidisplay.ui.viewModel.RowsListViewModel
import pl.pgorny.fixerioapidisplay.util.*
import timber.log.Timber

class RowsListFragment : Fragment() {
    private val eventLiveData =
        SingleLiveEvent<Event>()

    private val viewModel by viewModels<RowsListViewModel>(factoryProducer = {
        RowsListViewModel.Factory(
            getString(R.string.api_access_key),
            eventLiveData
        )
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRowsListBinding>(inflater,
            R.layout.fragment_rows_list, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        eventLiveData.observe(viewLifecycleOwner) {
            handleEvent(it)
        }

        val adapter = RowAdapter(eventLiveData)
        binding.rowsListFragmentRowsList.adapter = adapter
        viewModel.rows.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return binding.root
    }

    private fun handleEvent(event: Event){
        when(event){
            is ShowRateDetailsEvent ->
                findNavController().navigate(
                    R.id.action_rates_list_dest_to_rate_fragment_dest,
                    bundleOf(
                        "rate" to event.rate
                    )
                )
            is ShowApiErrorEvent ->
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
            is LoadingNextPage ->
                viewModel.progressBarVisible.postValue(true)
            is FinishedLoadingNextPage ->
                viewModel.progressBarVisible.postValue(false)
        }
    }
}