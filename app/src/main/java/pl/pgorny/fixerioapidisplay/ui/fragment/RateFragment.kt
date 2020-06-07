package pl.pgorny.fixerioapidisplay.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import pl.pgorny.fixerioapidisplay.R
import pl.pgorny.fixerioapidisplay.data.model.Rate
import pl.pgorny.fixerioapidisplay.databinding.FragmentRateBinding
import pl.pgorny.fixerioapidisplay.ui.viewModel.RateViewModel

class RateFragment : Fragment() {

    private val rate by lazy {
        requireArguments().getParcelable<Rate>("rate")!!
    }

    private val viewModel by viewModels<RateViewModel>(factoryProducer = {
        RateViewModel.Factory(
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