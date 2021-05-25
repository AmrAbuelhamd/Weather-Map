package com.blogspot.soyamr.weathermap.presentation.fragments.city_weather

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResultListener
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.databinding.FragmentCityWeatherDetailsBinding
import com.blogspot.soyamr.weathermap.presentation.base.BaseFragment
import com.blogspot.soyamr.weathermap.presentation.fragments.map.MapsFragment
import org.koin.android.ext.android.get


class CityWeatherDetailsFragment :
    BaseFragment<CityWeatherDetailsViewModel, FragmentCityWeatherDetailsBinding>(
        R.layout.fragment_city_weather_details
    ) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CITY_REQUEST_KEY) { _, bundle ->
            val cityName = bundle.getString(CITY_NAME_BUNDLE_KEY)
            viewModel.setCityWeatherInfo(cityName)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener()
        {
            requireActivity().supportFragmentManager.commit {
                replace<MapsFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
            }
        }
    }

    override fun setViewModel() {
        viewModel = get()
        binding.viewModel = viewModel
    }

    companion object {
        const val CITY_REQUEST_KEY = "city_request_key"
        const val CITY_NAME_BUNDLE_KEY = "city_name_key"
    }
}