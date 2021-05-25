package com.blogspot.soyamr.weathermap.presentation.fragments.city_weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blogspot.soyamr.domain.usecases.GetCityWeatherByName
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.presentation.base.BaseViewModel
import com.blogspot.soyamr.weathermap.presentation.converters.toPresenter
import com.blogspot.soyamr.weathermap.presentation.models.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityWeatherDetailsViewModel(
    private val getCityWeatherByName: GetCityWeatherByName
) : BaseViewModel() {

    private val _cityWeatherInfo: MutableLiveData<Weather> = MutableLiveData()
    val cityWeatherInfo: LiveData<Weather> = _cityWeatherInfo

    fun setCityWeatherInfo(cityName: String?) {
        if (cityName.isNullOrBlank()) {
            _errorMessage.postValue(R.string.something_went_wrong)
        } else {
            viewModelScope.launch(Dispatchers.IO + handler) {
                _cityWeatherInfo.postValue(getCityWeatherByName(cityName).toPresenter())
            }
        }
    }
}