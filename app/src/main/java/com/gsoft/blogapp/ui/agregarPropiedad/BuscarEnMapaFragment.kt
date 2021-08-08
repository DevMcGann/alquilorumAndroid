package com.gsoft.blogapp.ui.agregarPropiedad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gsoft.blogapp.R
import com.gsoft.blogapp.databinding.FragmentAgregarFotosBinding
import com.gsoft.blogapp.databinding.FragmentBuscarEnMapaBinding



class BuscarEnMapaFragment : Fragment(R.layout.fragment_buscar_en_mapa) {
    private lateinit var binding : FragmentBuscarEnMapaBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBuscarEnMapaBinding.bind(view)

        binding.bUbicarEnMapa.setOnClickListener(){
            findNavController().navigate(R.id.action_buscarEnMapaFragment_to_mapsFragment)
        }

        binding.bVolverAGaleria.setOnClickListener(){
            findNavController().navigate(R.id.action_buscarEnMapaFragment_to_agregarFotosFragment)
        }
    }
}