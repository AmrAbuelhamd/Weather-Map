package com.blogspot.soyamr.weathermap.presentation.map

import android.app.Activity.RESULT_OK
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding

    private lateinit var googleMap: GoogleMap

    private val locationManger: LocationManger by lazy {
        LocationManger(
            requireContext(),
            addMarker,
            resolutionForResult,
            requestPermissionLauncher,
            showMessage
        )
    }

    //callbacks
    private val addMarker: (userLocation: Location) -> Unit =
        {
            binding.progressCircular.isVisible = false
            val userLocation = LatLng(it.latitude, it.longitude)
            googleMap.addMarker(MarkerOptions().position(userLocation).title("you are Here "))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F));
        }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                locationManger.setMarkerOnMap()
            } else {
                showMessage(R.string.can_not_find_location,false)
            }
        }

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        locationManger.setMarkerOnMap()
    }

    private val resolutionForResult: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                locationManger.startLocationUpdates()
            } else {
                showMessage(R.string.can_not_find_location,false)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    val showMessage: (msgId: Int, progressBarVisible: Boolean) -> Unit =
        { mgsId, isVisible ->
            binding.progressCircular.isVisible = isVisible
            Toast.makeText(requireContext(), getString(mgsId), Toast.LENGTH_SHORT).show()
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