package com.blogspot.soyamr.weathermap.presentation.map.helpers

import android.content.Context
import android.content.IntentSender
import android.os.Looper
import com.blogspot.soyamr.weathermap.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class LocationManger(
    private val context: Context,
    private val mapFragmentListener: IMapFragmentListener
) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val locationCallback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                var showErrorMessage = true
                for (location in locationResult.locations) {
                    if (location != null) {
                        mapFragmentListener.setNewLocation(location)
                        fusedLocationClient.removeLocationUpdates(this)
                        showErrorMessage = false
                        break
                    }
                }
                if (showErrorMessage)
                    mapFragmentListener.showMessage(R.string.can_not_find_location)
            }
        }
    }

    fun getUserLocation() {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    mapFragmentListener.setNewLocation(it)
                } else {
                    requestLocationUpdates()
                }
            }
        } catch (e: SecurityException) {
            mapFragmentListener.showMessage(R.string.something_went_wrong)
        }
    }

    private fun requestLocationUpdates() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { _ ->
            startLocationUpdates()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    mapFragmentListener.changeUserSettings(exception)
                } catch (sendEx: IntentSender.SendIntentException) {
                    mapFragmentListener.showMessage(R.string.can_not_find_location)
                }
            }
        }
    }

    fun startLocationUpdates() {
        if (!mapFragmentListener.hasPermission()) {
            mapFragmentListener.showMessage(R.string.can_not_find_location)
            return
        }
        mapFragmentListener.showMessage(R.string.getting_your_location, true)
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            mapFragmentListener.showMessage(R.string.something_went_wrong)
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}