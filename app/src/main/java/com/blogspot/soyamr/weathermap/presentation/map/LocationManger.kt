package com.blogspot.soyamr.weathermap.presentation.map

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ActivityCompat
import com.blogspot.soyamr.weathermap.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class LocationManger(
    private val context: Context,
    private val addMarker: (userLocation: Location) -> Unit,
    private val resolutionForResult: ActivityResultLauncher<IntentSenderRequest>,
    private val requestPermissionLauncher: ActivityResultLauncher<String>,
    private val showMessage: (msgId: Int, progressBarVisible: Boolean) -> Unit
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
                var flag = false
                for (location in locationResult.locations) {
                    if (location != null) {
                        addMarker(location)
                        fusedLocationClient.removeLocationUpdates(this)
                        flag = true
                        break
                    }
                }
                if (flag)
                    showMessage(R.string.can_not_find_location, false)
            }
        }
    }

    fun setMarkerOnMap() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                addMarker(it)
            } else {
                requestLocationUpdates()
            }
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
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    resolutionForResult.launch(intentSenderRequest)

                } catch (sendEx: IntentSender.SendIntentException) {
                    showMessage(R.string.can_not_find_location, false)
                }
            }
        }
    }

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showMessage(R.string.can_not_find_location, false)
            return
        }
        showMessage(R.string.getting_your_location, true)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}