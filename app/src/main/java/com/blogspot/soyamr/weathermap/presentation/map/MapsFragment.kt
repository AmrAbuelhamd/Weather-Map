package com.blogspot.soyamr.weathermap.presentation.map

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.databinding.FragmentMapsBinding
import com.blogspot.soyamr.weathermap.presentation.map.helpers.LocationListener
import com.blogspot.soyamr.weathermap.presentation.map.helpers.LocationManger
import com.blogspot.soyamr.weathermap.presentation.utils.bitMapFromVector
import com.blogspot.soyamr.weathermap.presentation.utils.getLocationAsDMS
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding

    private lateinit var googleMap: GoogleMap

    private val locationManagerListener = object : LocationListener {
        override fun onLocationUpdated(userLocation: Location) {
            binding.progressCircular.isVisible = false
            val userLocationLatLng = LatLng(userLocation.latitude, userLocation.longitude)
            googleMap.addMarker(MarkerOptions().position(userLocationLatLng))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocationLatLng))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
        }

        override fun hasLocationPermission() =
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        override fun askUserToOpenGPS(exception: ResolvableApiException) {
            val intentSenderRequest =
                IntentSenderRequest.Builder(exception.resolution).build()
            resolutionForResult.launch(intentSenderRequest)
        }

        override fun onSomethingWentWrong() {
            showMessage(R.string.can_not_find_location, false)

        }

        override fun onGettingLocation() {
            showMessage(R.string.getting_your_location, true)
        }
    }

    private val locationManger: LocationManger by lazy {
        LocationManger(
            requireContext(),
            locationManagerListener
        )
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                locationManger.getUserLocation()
            } else {
                locationManagerListener.onSomethingWentWrong()
            }
        }

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        if (locationManagerListener.hasLocationPermission()) {
            locationManger.getUserLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        googleMap.setOnMapClickListener {
            onUserClickOnMap(it)
        }
    }

    private fun onUserClickOnMap(latLng: LatLng) {
        binding.floatingCityInfoContainer.isGone = true
        googleMap.clear()
        googleMap.addMarker(
            MarkerOptions().position(latLng)
                .icon(bitMapFromVector(R.drawable.ic_pin, requireContext()))
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
        showCityNameIfExists(latLng)
    }

    private fun showCityNameIfExists(latLng: LatLng) {
        if(Geocoder.isPresent()) {
            try {
                val geocoder = Geocoder(requireContext())
                val addresses: List<Address> =
                    geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (addresses.isEmpty())
                    return//show nothing

                val cityName: String = addresses[0].locality

                binding.floatingCityInfoContainer.isVisible = true
                binding.cityNameTextView.text = cityName
                binding.locationTextView.text = getLocationAsDMS(Location("").also {
                    it.longitude = latLng.longitude
                    it.latitude = latLng.latitude
                }, 1)
                binding.closeButton.setOnClickListener {
                    binding.floatingCityInfoContainer.isGone = true
                }
                binding.openCityWeatherFragmentButton.setOnClickListener { }


            } catch (e: Exception) {
                //show nothing
                e.printStackTrace()
            }
        }else{
            showMessage(R.string.googleServiceNotFound,false)
        }
    }


    private val resolutionForResult: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                locationManger.startLocationUpdates()
            } else {
                locationManagerListener.onSomethingWentWrong()
            }
        }

    private fun showMessage(msgId: Int, showProgressBar: Boolean) {
        binding.progressCircular.isVisible = showProgressBar
        Toast.makeText(
            requireContext(), getString(msgId), Toast.LENGTH_SHORT
        ).show()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMapsBinding.inflate(inflater).run {
            binding = this
            root
        }
    }

    override fun onPause() {
        super.onPause()
        locationManger.stopLocationUpdates()
    }

}
