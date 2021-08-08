package com.gsoft.blogapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gsoft.blogapp.R
import com.gsoft.blogapp.databinding.FragmentHomeScreenBinding
import com.gsoft.blogapp.databinding.FragmentLoginScreenBinding

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {
    private lateinit var binding : FragmentHomeScreenBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)

        goToPropiedades()
        goToProfile()
    }

    private fun goToPropiedades(){
        binding.bBuscar.setOnClickListener{
            findNavController().navigate(R.id.action_homeScreenFragment_to_propiedadesScreenFragment)
        }
    }

    private fun goToProfile(){
        binding.bIniciarSesion.setOnClickListener{
            findNavController().navigate(R.id.action_homeScreenFragment_to_loginScreenFragment)
        }
    }
}