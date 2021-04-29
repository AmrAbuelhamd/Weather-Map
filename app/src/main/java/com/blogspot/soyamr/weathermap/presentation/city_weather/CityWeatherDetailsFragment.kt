package com.blogspot.soyamr.weathermap.presentation.city_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blogspot.soyamr.weathermap.databinding.FragmentCityWeatherDetailsBinding
import org.koin.android.ext.android.get


class CityWeatherDetailsFragment : Fragment() {

    lateinit var viewModel: CityWeatherDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)

        return FragmentCityWeatherDetailsBinding.inflate(inflater).also {
            viewModel = get()
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }.root
    }

}