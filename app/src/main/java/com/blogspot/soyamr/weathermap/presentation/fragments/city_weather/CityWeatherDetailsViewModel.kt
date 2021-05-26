package com.blogspot.soyamr.weathermap.presentation.fragments.city_weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blogspot.soyamr.data.utils.NoInternetException
import com.blogspot.soyamr.domain.usecases.GetCityWeatherByName
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.presentation.converters.toPresenter
import com.blogspot.soyamr.weathermap.presentation.models.Weather
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityWeatherDetailsViewModel(
    private val getCityWeatherByName: GetCityWeatherByName
) : ViewModel() {

    private val _errorMessage: MutableLiveData<Int> = MutableLiveData(0)
    val errorMessage: LiveData<Int> = _errorMessage

    private val _progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData(true)
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    private val _cityWeatherInfo: MutableLiveData<Weather> = MutableLiveData()
    val cityWeatherInfo: LiveData<Weather> = _cityWeatherInfo

    val handler = CoroutineExceptionHandler { _, exception ->
        if (exception is NoInternetException)
            _errorMessage.postValue(R.string.no_internet)
        else
            _errorMessage.postValue(R.string.something_went_wrong)
    }

    fun switchProgressBarVisibility(visibility: Boolean) {
        _progressBarVisibility.value = visibility
    }

    fun setCityWeatherInfo(cityName: String?) {
        if (cityName.isNullOrBlank()) {
            _errorMessage.postValue(R.string.something_went_wrong)
        } else {
            _progressBarVisibility.value = true
            viewModelScope.launch(Dispatchers.IO + handler) {
                _cityWeatherInfo.postValue(getCityWeatherByName(cityName).toPresenter())
                _progressBarVisibility.postValue(false)
            }
        }
    }
}