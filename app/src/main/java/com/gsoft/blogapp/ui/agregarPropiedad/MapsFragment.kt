package com.gsoft.blogapp.ui.agregarPropiedad

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gsoft.blogapp.R
import com.gsoft.blogapp.data.datasource.remote.propiedades.PropiedadesDataSource
import com.gsoft.blogapp.databinding.FragmentMapsBinding
import com.gsoft.blogapp.domain.propiedades.PropiedadesRepoImpl
import com.gsoft.blogapp.presentation.agregarPropiedad.AgregarPropiedadViewModel
import com.gsoft.blogapp.presentation.propiedades.PropiedadesScreenViewModelFactory

class MapsFragment : Fragment(R.layout.fragment_maps),OnMapReadyCallback {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private val viewModel by activityViewModels<AgregarPropiedadViewModel>{
        PropiedadesScreenViewModelFactory(
            PropiedadesRepoImpl( PropiedadesDataSource())
        )
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var miLocation : LatLng? = null
    var markerPosition : LatLng? = null
    private val zoom : Float =  18f
    private val time : Int = 4000
    private lateinit var binding : FragmentMapsBinding
    private lateinit var mMap: GoogleMap


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapsBinding.bind(view)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "No hay permisos de Geolocalizacion", Toast.LENGTH_SHORT).show()
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            val lat = it.latitude
            val long = it.longitude
            miLocation = LatLng(lat,long)
            whereIAm(miLocation!!)
            Log.d("LOC", "lat ${lat} - long ${long}  milocation = ${miLocation}" )
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "No hay permisos de Geolocalizacion", Toast.LENGTH_SHORT).show()
        }

        createMapFragment()

        binding.bIrAPublicar.setOnClickListener {
            viewModel.setLatLong(markerPosition?.latitude.toString(), markerPosition?.longitude.toString()  )
            findNavController().navigate(R.id.action_mapsFragment_to_pagarFragment)
        }
    }

    private fun createMapFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(mapa: GoogleMap?) {
        if (mapa != null) {
            mMap = mapa

            enableMyLocation()

            mapa.setOnMapClickListener {
                mMap.clear()
                val lat = it.latitude
                val long = it.longitude
                val latLong  = LatLng(lat,long)
                val marker = MarkerOptions()
                    .position(latLong)
                this.mMap.addMarker(marker)
                markerPosition = LatLng(lat, long)
                binding.bIrAPublicar.isVisible = true
            }
        }
    }

    private fun whereIAm(myposition : LatLng) {
        // mMap.addMarker(MarkerOptions().position(myposition).title("Marcador"))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(myposition, zoom),
            time,
            null
        )
    }


    private fun isPermissionsGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableMyLocation() {
        if (!::mMap.isInitialized) return
        if (isPermissionsGranted()) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            mMap.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(requireContext(), "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(requireContext(), "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        if (!::mMap.isInitialized) return
        if(!isPermissionsGranted()){
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            mMap.isMyLocationEnabled = false
            Toast.makeText(requireContext(), "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }

    }

}