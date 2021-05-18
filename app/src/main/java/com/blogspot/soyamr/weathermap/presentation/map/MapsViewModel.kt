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
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapsViewModel(
    private val geocoder: Geocoder,
    private val placesClient: PlacesClient
) :
    ViewModel() {

    private val _cityInfoContainerVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    val cityInfoContainerVisibility: LiveData<Boolean> = _cityInfoContainerVisibility

    private val _progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    private val _cityName: MutableLiveData<String> = MutableLiveData("")
    val cityName: LiveData<String> = _cityName

    private val _locationFormattedString: MutableLiveData<String> = MutableLiveData("")
    val locationFormattedString: LiveData<String> = _locationFormattedString

    private val _errorMessage: MutableLiveData<Int> = MutableLiveData(0)
    val errorMessage: LiveData<Int> = _errorMessage

    private val _suggestions: MutableLiveData<List<Place>> = MutableLiveData(ArrayList())
    val suggestions: LiveData<List<Place>> = _suggestions

    private val _currentLatLng: MutableLiveData<LatLng> = MutableLiveData()
    val currentLatLng: LiveData<LatLng> = _currentLatLng

    val showWeatherDetails: SingleLiveEvent<String> = SingleLiveEvent()

    private var querySearchJob: Job? = null

    // Specify the fields to return.
    private val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

    fun showCityNameIfExists(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            _cityInfoContainerVisibility.postValue(false)
            if (Geocoder.isPresent()) {
                try {
                    val addresses: List<Address> =
                        geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    if (addresses.isEmpty()) {
                        _errorMessage.postValue(R.string.no_city_found)
                        return@launch
                    }
                    _cityInfoContainerVisibility.postValue(true)
                    _cityName.postValue(addresses[0].locality)
                    _locationFormattedString.postValue(
                        getLocationAsDMS(Location("").also {
                            it.longitude = latLng.longitude
                            it.latitude = latLng.latitude
                        })
                    )
                } catch (e: Exception) {
                    _errorMessage.postValue(R.string.something_went_wrong)
                    e.printStackTrace()
                }
            } else {
                _errorMessage.postValue(R.string.googleServiceNotFound)
            }
        }
    }

    fun onCloseButtonClick() {
        _cityInfoContainerVisibility.value = false
    }

    fun openWeatherFragment() {
        if (!cityName.value.isNullOrBlank())
            showWeatherDetails.value = cityName.value
        else
            _errorMessage.value = R.string.choose_city
    }

    fun switchProgressBarVisibility(visibility: Boolean) {
        _progressBarVisibility.value = visibility
    }

    fun searchFor(query: String) {
        querySearchJob?.cancel()
        _cityInfoContainerVisibility.value = false
        querySearchJob = viewModelScope.launch {

            _suggestions.value = ArrayList()

            val token = AutocompleteSessionToken.newInstance()
            val request =
                FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.REGIONS)
                    .setSessionToken(token)
                    .setQuery(query)
                    .build()
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    for (prediction: AutocompletePrediction in response.autocompletePredictions) {
                        setPlaceSuggestion(prediction.placeId)
                    }
                }.addOnFailureListener { exception: Exception? ->
                    if (exception is ApiException) {
                        _errorMessage.value = (R.string.something_went_wrong)
                    }
                }
        }
    }

    fun showCity(cityPositionInSuggestions: Int) {
        val currentPlace = suggestions.value?.get(cityPositionInSuggestions)
        if (currentPlace == null) {
            _errorMessage.value = (R.string.something_went_wrong)
            return
        }

        val currentPlaceLatLng = currentPlace.latLng
        if (currentPlaceLatLng == null) {
            _errorMessage.value = (R.string.something_went_wrong)
            return
        }

        _cityInfoContainerVisibility.value = true
        _currentLatLng.value = currentPlaceLatLng
        _cityName.value = currentPlace.name
        _locationFormattedString.value = getLocationAsDMS(Location("").also {
            it.longitude = currentPlaceLatLng.longitude
            it.latitude = currentPlaceLatLng.latitude
        })
    }

    private fun setPlaceSuggestion(placeId: String) {
        // Construct a request object, passing the place ID and fields array.
        val requestObject = FetchPlaceRequest.newInstance(placeId, placeFields)
        placesClient.fetchPlace(requestObject)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                _suggestions.value = _suggestions.value?.plus(response.place)
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    _errorMessage.value = (R.string.something_went_wrong)
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
        formattedCoordinates = formattedCoordinates
            .replaceFirst(":".toRegex(), "°")
            .replaceFirst(":".toRegex(), "'")
        val pointIndex = formattedCoordinates.indexOf(".")
        val endIndex = pointIndex + 1 + decimalPlace
        if (endIndex < formattedCoordinates.length) {
            formattedCoordinates = formattedCoordinates.substring(0, endIndex)
        }
        formattedCoordinates += "\""
        return formattedCoordinates
    }
}
