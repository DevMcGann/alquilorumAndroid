package com.gsoft.blogapp.ui.agregarPropiedad

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gsoft.blogapp.R
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.data.datasource.remote.propiedades.PropiedadesDataSource
import com.gsoft.blogapp.data.models.Propiedad
import com.gsoft.blogapp.databinding.FragmentBuscarEnMapaBinding
import com.gsoft.blogapp.databinding.FragmentPagarBinding
import com.gsoft.blogapp.domain.propiedades.PropiedadesRepoImpl
import com.gsoft.blogapp.presentation.agregarPropiedad.AgregarPropiedadViewModel
import com.gsoft.blogapp.presentation.propiedades.PropiedadesScreenViewModelFactory


class PagarFragment : Fragment(R.layout.fragment_pagar) {
    private lateinit var binding : FragmentPagarBinding

    private val viewModel by activityViewModels<AgregarPropiedadViewModel>{
        PropiedadesScreenViewModelFactory(
                PropiedadesRepoImpl( PropiedadesDataSource())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPagarBinding.bind(view)

        binding.bMercadoPago.setOnClickListener(){
            try {
                uploadAndCreate()
            }catch (e:Exception){
                Toast.makeText(requireContext(), "Error al crear Propiedad", Toast.LENGTH_LONG).show()

            }
        }

        binding.bVolverAMapa.setOnClickListener(){
            findNavController().navigate(R.id.action_pagarFragment_to_buscarEnMapaFragment)
        }
    }

    private fun uploadAndCreate() {
        viewModel.uploadImagesToStorageAndGetURL().observe(viewLifecycleOwner, Observer { result ->
            when (result) {

                is Result.Loading -> {
                    binding.progressBar2.visibility = View.VISIBLE
                    binding.tCargando.visibility = View.VISIBLE
                    binding.bVolverAMapa.visibility = View.GONE
                    binding.bMercadoPago.visibility = View.GONE
                }

                is Result.Success -> {
                    viewModel.setImagenes(result.data)
                    if (viewModel.getImagenes().isNotEmpty()) {
                        viewModel.finalizarUpload()
                        binding.progressBar2.visibility = View.GONE
                        binding.tCargando.visibility = View.GONE
                        Toast.makeText(requireContext(), "Propiedad Creada", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_pagarFragment_to_profileScreenFragment)
                        binding.bVolverAMapa.visibility = View.VISIBLE
                        binding.bMercadoPago.visibility = View.VISIBLE

                    }else{
                        Toast.makeText(requireContext(), "Error cargando Imágenes!", Toast.LENGTH_LONG).show()
                        binding.progressBar2.visibility = View.GONE
                        binding.tCargando.visibility = View.GONE
                        binding.bVolverAMapa.visibility = View.VISIBLE
                        binding.bMercadoPago.visibility = View.VISIBLE

                    }

                }

                is Result.Failure -> {
                    binding.progressBar2.visibility = View.GONE
                    binding.tCargando.visibility = View.GONE
                    binding.bVolverAMapa.visibility = View.VISIBLE
                    binding.bMercadoPago.visibility = View.VISIBLE
                    Toast.makeText(
                            requireContext(),
                            "Hubo un error : ${result.exception}",
                            Toast.LENGTH_LONG
                    ).show()

                }
            }
        })
    }
}