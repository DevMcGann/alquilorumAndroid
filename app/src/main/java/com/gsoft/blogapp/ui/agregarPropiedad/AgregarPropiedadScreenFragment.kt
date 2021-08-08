package com.gsoft.blogapp.ui.agregarPropiedad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.gsoft.blogapp.R
import com.gsoft.blogapp.data.datasource.remote.propiedades.PropiedadesDataSource
import com.gsoft.blogapp.databinding.FragmentAgregarPropiedadScreenBinding
import com.gsoft.blogapp.domain.propiedades.PropiedadesRepoImpl
import com.gsoft.blogapp.presentation.agregarPropiedad.AgregarPropiedadViewModel
import com.gsoft.blogapp.presentation.propiedades.PropiedadesScreenViewModelFactory


class AgregarPropiedadScreenFragment : Fragment(R.layout.fragment_agregar_propiedad_screen) {
    private lateinit var binding : FragmentAgregarPropiedadScreenBinding
    private val viewModel by activityViewModels<AgregarPropiedadViewModel>{
        PropiedadesScreenViewModelFactory(
                PropiedadesRepoImpl( PropiedadesDataSource())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAgregarPropiedadScreenBinding.bind(view)
        goBackToHome()
        bIrAGaleria()
    }



    private fun bIrAGaleria() {
        binding.bIrAImagenes.setOnClickListener{
            if (binding.tdireccion.text.isNullOrEmpty() || binding.tprecio.text.isNullOrEmpty() || binding.tdesc.text.isNullOrEmpty() ){
                Toast.makeText(activity, "Debes llenar todos los campos para continuar", Toast.LENGTH_LONG).show()
            }else{
                viewModel.setDireccionPrecioDescOwnerId(
                        dir = binding.tdireccion.text.toString(),
                        prec = binding.tprecio.text.toString().toInt(),
                        descr = binding.tdesc.text.toString(),
                        own = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                        contact = binding.tcontacto.text.toString()
                )
                findNavController().navigate(R.id.action_agregarPropiedadScreenFragment_to_agregarFotosFragment)
            }
        }

    }

    private fun goBackToHome(){
        binding.bbacktoprofile.setOnClickListener{
            findNavController().navigate(R.id.action_agregarPropiedadScreenFragment_to_profileScreenFragment)
        }
    }


}