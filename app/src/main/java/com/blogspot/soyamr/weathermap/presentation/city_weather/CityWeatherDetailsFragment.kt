package com.blogspot.soyamr.weathermap.presentation.city_weather

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.databinding.FragmentCityWeatherDetailsBinding
import com.blogspot.soyamr.weathermap.presentation.base.BaseFragment
import org.koin.android.ext.android.get


class CityWeatherDetailsFragment :
    BaseFragment<CityWeatherDetailsViewModel, FragmentCityWeatherDetailsBinding>(
        R.layout.fragment_city_weather_details
    ) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CITY_REQUEST_KEY) { _, bundle ->
            val cityName = bundle.getString(CITY_NAME_BUNDLE_KEY)
            viewModel.setCityWeatherInfo(cityName)// viewModel is initialized inside onCreateView
                                // how does it still work even though it's not initialized yet?
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