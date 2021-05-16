package com.blogspot.soyamr.weathermap.presentation.map

import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.presentation.utils.SingleLiveEvent
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsViewModel(private val geocoder: Geocoder) : ViewModel() {

    val cityInfoContainerVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    val cityName: MutableLiveData<String> = MutableLiveData("")
    val locationFormattedString: MutableLiveData<String> = MutableLiveData("")

    private val _errorMessage: MutableLiveData<Int> = MutableLiveData(0)
    val errorMessage: LiveData<Int> = _errorMessage

    val showWeatherDetails: SingleLiveEvent<String> = SingleLiveEvent()

    fun showCityNameIfExists(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                cityInfoContainerVisibility.value = false
                if (Geocoder.isPresent()) {
                    try {
                        val addresses: List<Address> = withContext(Dispatchers.IO) {
                            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                        }
                        if (addresses.isEmpty()) {
                            _errorMessage.value = R.string.no_city_found
                            return@withContext
                        }
                        cityInfoContainerVisibility.value = true
                        cityName.value = addresses[0].locality
                        locationFormattedString.value =
                            getLocationAsDMS(Location("").also {
                                it.longitude = latLng.longitude
                                it.latitude = latLng.latitude
                            })
                    } catch (e: Exception) {
                        _errorMessage.value = R.string.something_went_wrong
                        e.printStackTrace()
                    }
                } else {
                    _errorMessage.value = R.string.googleServiceNotFound
                }
            }
        }
    }

    private fun getLocationAsDMS(location: Location, decimalPlace: Int = 1): String {
        var strLatitude = Location.convert(location.latitude, Location.FORMAT_SECONDS)
        strLatitude = replaceDelimiters(strLatitude, decimalPlace)
        val latCardinal = if (location.longitude >= 0) 'N' else 'S'
        strLatitude = "$strLatitude $latCardinal"
        var strLongitude = Location.convert(location.longitude, Location.FORMAT_SECONDS)
        strLongitude = replaceDelimiters(strLongitude, decimalPlace)
        val lonCardinal = if (location.longitude >= 0) 'E' else 'W'
        strLongitude = "$strLongitude $lonCardinal"
        return "$strLatitude $strLongitude"
    }

    private fun replaceDelimiters(coordinateString: String, decimalPlace: Int): String {
        var formattedCoordinates = coordinateString
        formattedCoordinates = formattedCoordinates.replaceFirst(":".toRegex(), "°")
        formattedCoordinates = formattedCoordinates.replaceFirst(":".toRegex(), "'")
        val pointIndex = formattedCoordinates.indexOf(".")
        val endIndex = pointIndex + 1 + decimalPlace
        if (endIndex < formattedCoordinates.length) {
            formattedCoordinates = formattedCoordinates.substring(0, endIndex)
        }
        formattedCoordinates += "\""
        return formattedCoordinates
    }

    fun onCloseButtonClick() {
        cityInfoContainerVisibility.value = false
    }

    fun showMoreData() {
        if (!cityName.value.isNullOrBlank())
            showWeatherDetails.value = cityName.value
        else
            _errorMessage.value = R.string.choose_city
    }
}