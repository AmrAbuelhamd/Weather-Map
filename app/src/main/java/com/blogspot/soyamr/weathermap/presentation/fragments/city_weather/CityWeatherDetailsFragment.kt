package com.blogspot.soyamr.weathermap.presentation.fragments.city_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResultListener
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.databinding.FragmentCityWeatherDetailsBinding
import com.blogspot.soyamr.weathermap.presentation.fragments.map.MapsFragment
import org.koin.android.viewmodel.ext.android.viewModel


class CityWeatherDetailsFragment : Fragment() {

    private val viewModel: CityWeatherDetailsViewModel by viewModel()
    private lateinit var binding: FragmentCityWeatherDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CITY_REQUEST_KEY) { _, bundle ->
            val cityName = bundle.getString(CITY_NAME_BUNDLE_KEY)
            viewModel.setCityWeatherInfo(cityName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityWeatherDetailsBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.toolbar.setNavigationOnClickListener()
        {
            requireActivity().supportFragmentManager.commit {
                replace<MapsFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
            }
        }
        observeViewModelLiveData()

        return binding.root
    }

    private fun observeViewModelLiveData() {
        viewModel.errorMessage.observe(viewLifecycleOwner, ::showError)
    }

    private fun showError(errorStringId: Int?) {
        errorStringId?.let {
            if (it != 0) {
                showMessage(it, false)
            }
        }
    }

    private fun showMessage(msgId: Int, showProgressBar: Boolean) {
        viewModel.switchProgressBarVisibility(showProgressBar)
        Toast.makeText(
            requireContext(), getString(msgId), Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val CITY_REQUEST_KEY = "city_request_key"
        const val CITY_NAME_BUNDLE_KEY = "city_name_key"
    }
}