package com.blogspot.soyamr.weathermap.presentation.city_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.blogspot.soyamr.weathermap.databinding.FragmentCityWeatherDetailsBinding
import org.koin.android.ext.android.get


class CityWeatherDetailsFragment : Fragment() {

    lateinit var viewModel: CityWeatherDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = get()
        setFragmentResultListener(CITY_REQUEST_KEY) { _, bundle ->
            val cityName = bundle.getString(CITY_NAME_BUNDLE_KEY)
            viewModel.setCityWeatherInfo(cityName)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentCityWeatherDetailsBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }.root
    }

    companion object{
        const val CITY_REQUEST_KEY = "city_request_key"
        const val CITY_NAME_BUNDLE_KEY = "city_name_key"
    }
}