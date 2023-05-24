package com.example.ecoassistant

import androidx.fragment.app.Fragment
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*


class MapsFragment : Fragment() {

    private val TAG = "MapsFragment"

    private lateinit var googleMap: GoogleMap
    private lateinit var database: FirebaseDatabase
    private lateinit var markersReference: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var markerList: MutableList<MarkerOptions>

    private val callback = OnMapReadyCallback { googleMap ->

        Log.d(TAG, "onMapReady")

        googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                val view = layoutInflater.inflate(R.layout.custom_snippet, null)

                val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
                val snippetTextView = view.findViewById<TextView>(R.id.snippetTextView)

                titleTextView.text = marker.title
                snippetTextView.text = marker.snippet

                return view
            }
        })

        this.googleMap = googleMap

        database = FirebaseDatabase.getInstance()
        markersReference = database.reference.child("Markers")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        markersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                markerList = mutableListOf<MarkerOptions>()

                for (markerSnapshot in snapshot.children) {
                    //val id = markerSnapshot.child("id").getValue(Long::class.java)?.toString() ?: ""
                    val name = markerSnapshot.child("name").getValue(String::class.java) ?: ""
                    val company = markerSnapshot.child("company").getValue(String::class.java) ?: ""
                    val address = markerSnapshot.child("address").getValue(String::class.java) ?: ""
                    val latitude = markerSnapshot.child("latitude").getValue(Double::class.java)
                    val longitude = markerSnapshot.child("longitude").getValue(Double::class.java)

                    val markerPosition = if (latitude != null && longitude != null) {
                        LatLng(latitude, longitude)
                    } else {
                        LatLng(0.0, 0.0)
                    }
                    val markerOptions = MarkerOptions()
                        .position(markerPosition)
                        .title(name)
                        .snippet("$company\nAdrese: $address")

                    val markerIcon = when (name) {
                        "Tekstils" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                        "EKO laukums" -> BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_VIOLET
                        )

                        "Stikls" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                        "Plastmasa, metāls, papīrs" -> BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_YELLOW
                        )

                        else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    }

                    markerOptions.icon(markerIcon)

                    markerList.add(markerOptions)

                    for (markerOptions in markerList) {
                        googleMap.addMarker(markerOptions)
                    }

                }

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let {
                            val currentLatLng = LatLng(it.latitude, it.longitude)
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    currentLatLng,
                                    12f
                                )
                            )
                        }
                            ?: run {
                                if (markerList.isNotEmpty()) {
                                    val firstMarker = markerList[0]
                                    googleMap.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            firstMarker.position,
                                            12f
                                        )
                                    )
                                }
                            }
                    }
                } else {
                    if (markerList.isNotEmpty()) {
                        val firstMarker = markerList[0]
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                firstMarker.position,
                                12f
                            )
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}