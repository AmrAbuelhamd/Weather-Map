package com.blogspot.soyamr.weathermap.presentation.fragments.map.helpers

import android.location.Location
import com.google.android.gms.common.api.ResolvableApiException

interface LocationListener {
    fun onSomethingWentWrong()
    fun onGettingLocation()
    fun onLocationUpdated(userLocation: Location)
    fun hasLocationPermission(): Boolean
    fun askUserToOpenGPS(exception: ResolvableApiException)
}