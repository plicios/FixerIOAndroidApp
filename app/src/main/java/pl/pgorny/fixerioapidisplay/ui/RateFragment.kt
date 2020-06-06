package pl.pgorny.fixerioapidisplay.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import pl.pgorny.fixerioapidisplay.R
import pl.pgorny.fixerioapidisplay.data.model.DateRow
import pl.pgorny.fixerioapidisplay.data.model.RateRow
import pl.pgorny.fixerioapidisplay.databinding.FragmentRateBinding
import pl.pgorny.fixerioapidisplay.ui.viewModel.DateRowViewModel
import pl.pgorny.fixerioapidisplay.ui.viewModel.RateRowViewModel

class RateFragment : Fragment() {

    private val rate by lazy {
        requireArguments().getParcelable<RateRow>("rate")!!
    }

    private val viewModel by viewModels<RateRowViewModel>(factoryProducer = {
        RateRowViewModel.Factory(
            rate
        )
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRateBinding>(inflater,
            R.layout.fragment_rate, container, false)
        binding.viewModel = viewModel

        return binding.root
    }
}