package com.blogspot.soyamr.weathermap.presentation.map.helpers

import android.location.Location
import com.google.android.gms.common.api.ResolvableApiException

interface IMapFragmentListener {
    fun showMessage(msgId: Int, progressBarVisible: Boolean = false)
    fun setNewLocation(userLocation: Location)
    fun hasPermission(): Boolean
    fun changeUserSettings(exception: ResolvableApiException)
}