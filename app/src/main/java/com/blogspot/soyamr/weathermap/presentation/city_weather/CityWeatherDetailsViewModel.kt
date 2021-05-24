package com.blogspot.soyamr.weathermap.presentation.city_weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blogspot.soyamr.domain.models.Weather
import com.blogspot.soyamr.domain.usecases.GetCityWeatherByName
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityWeatherDetailsViewModel(
    private val getCityWeatherByName: GetCityWeatherByName
) : BaseViewModel() {

    private val _cityWeatherInfo: MutableLiveData<Weather> = MutableLiveData()
    val cityWeatherInfo: LiveData<Weather> = _cityWeatherInfo

    fun setCityWeatherInfo(cityName: String?) {
        if (cityName.isNullOrBlank()) {
            _errorMessage.value = R.string.something_went_wrong
        } else {
            viewModelScope.launch(Dispatchers.IO + handler) {
                _cityWeatherInfo.postValue(getCityWeatherByName(cityName))
            }
        }
    }
}