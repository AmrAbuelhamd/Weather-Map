package com.blogspot.soyamr.weathermap.presentation.map

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.SearchManager
import android.content.pm.PackageManager
import android.database.MatrixCursor
import android.location.Location
import android.os.Bundle
import android.provider.BaseColumns
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.databinding.FragmentMapsBinding
import com.blogspot.soyamr.weathermap.presentation.city_weather.CityWeatherDetailsFragment
import com.blogspot.soyamr.weathermap.presentation.map.helpers.LocationListener
import com.blogspot.soyamr.weathermap.presentation.map.helpers.LocationManger
import com.blogspot.soyamr.weathermap.presentation.utils.Utils.bitMapFromVector
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import org.koin.android.ext.android.get


class MapsFragment : Fragment() {

    lateinit var binding: FragmentMapsBinding

    private lateinit var googleMap: GoogleMap

    private val viewModel: MapsViewModel by lazy { get() }

    private val customPinIcon: BitmapDescriptor by lazy {
        bitMapFromVector(R.drawable.ic_pin, requireContext())
    }

    //search view variables
    private val searchView: SearchView by lazy {
        binding.toolbar.menu.findItem(R.id.searchView).actionView as SearchView
    }
    private val cursorAdapter: CursorAdapter by lazy {
        SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            null,
            arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1),
            intArrayOf(android.R.id.text1),
            0
        )
    }

    private val locationManagerListener = object : LocationListener {
        override fun onLocationUpdated(userLocation: Location) {
            viewModel.switchProgressBarVisibility(false)
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

    private val resolutionForResult: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                locationManger.startLocationUpdates()
            } else {
                locationManagerListener.onSomethingWentWrong()
            }
        }

    private fun onUserClickOnMap(latLng: LatLng) {
        showLocationOnMap(latLng)
        viewModel.showCityNameIfExists(latLng)
    }

    private fun showLocationOnMap(latLng: LatLng) {
        googleMap.clear()
        googleMap.addMarker(
            MarkerOptions().position(latLng)
                .icon(customPinIcon)
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
    }

    private fun showMessage(msgId: Int, showProgressBar: Boolean) {
        viewModel.switchProgressBarVisibility(showProgressBar)
        Toast.makeText(
            requireContext(), getString(msgId), Toast.LENGTH_SHORT
        ).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        setUpViewModelListeners()
        setUpSearchView()
    }

    private fun setUpSearchView() {
        searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text).threshold = 1
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.trim()?.length ?: 0 >= 2)
                    viewModel.searchFor(newText?.trim().toString())
                return true
            }
        })
        searchView.suggestionsAdapter = cursorAdapter

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                searchView.clearFocus()
                viewModel.showCity(position)
                return true
            }
        })
    }

    private fun setUpViewModelListeners() {
        viewModel.errorMessage.observe(viewLifecycleOwner, ::showError)
        viewModel.showWeatherDetails.observe(viewLifecycleOwner, ::openCityWeatherFragment)
        viewModel.suggestions.observe(viewLifecycleOwner, ::showSuggestion)
        viewModel.currentLatLng.observe(viewLifecycleOwner, ::showLocationOnMap)
    }

    private fun showSuggestion(suggestions: MutableList<Place>?) {
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
        suggestions?.forEachIndexed { index, suggestion ->
            cursor.addRow(arrayOf(index, suggestion.name))
        }
        cursorAdapter.changeCursor(cursor)
    }

    private fun openCityWeatherFragment(cityName: String?) {
        cityName?.let {
            requireActivity().supportFragmentManager.commit {
                replace<CityWeatherDetailsFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
            }
        }
    }

    private fun showError(errorStringId: Int?) {
        errorStringId?.let {
            if (it != 0) {
                showMessage(it, false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMapsBinding.inflate(inflater).run {
            viewModel = this@MapsFragment.viewModel
            lifecycleOwner = this@MapsFragment
            this@MapsFragment.binding = this
            root
        }
    }

    override fun onPause() {
        super.onPause()
        locationManger.stopLocationUpdates()
    }
}
