package com.blogspot.soyamr.weathermap.presentation.map

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.databinding.FragmentMapsBinding
import com.blogspot.soyamr.weathermap.presentation.map.helpers.IMapFragmentListener
import com.blogspot.soyamr.weathermap.presentation.map.helpers.LocationManger
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), IMapFragmentListener {

    private lateinit var binding: FragmentMapsBinding

    private lateinit var googleMap: GoogleMap

    private val locationManger: LocationManger by lazy {
        LocationManger(
            requireContext(),
            this@MapsFragment
        )
    }

    override fun setNewLocation(userLocation: Location) {
        binding.progressCircular.isVisible = false
        val userLocationLatLng = LatLng(userLocation.latitude, userLocation.longitude)
        googleMap.addMarker(MarkerOptions().position(userLocationLatLng))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocationLatLng))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                locationManger.getUserLocation()
            } else {
                showMessage(R.string.can_not_find_location, false)
            }
        }

    override fun hasPermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    override fun changeUserSettings(exception: ResolvableApiException) {
        val intentSenderRequest =
            IntentSenderRequest.Builder(exception.resolution).build()
        resolutionForResult.launch(intentSenderRequest)
    }


    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        if (hasPermission()) {
            locationManger.getUserLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val resolutionForResult: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                locationManger.startLocationUpdates()
            } else {
                showMessage(R.string.can_not_find_location, false)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun showMessage(msgId: Int, progressBarVisible: Boolean) {
        binding.progressCircular.isVisible = progressBarVisible
        Toast.makeText(requireContext(), getString(msgId), Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        locationManger.stopLocationUpdates()
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
}