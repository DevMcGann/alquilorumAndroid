package com.gsoft.blogapp.ui.agregarPropiedad

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gsoft.blogapp.R
import com.gsoft.blogapp.data.datasource.remote.propiedades.PropiedadesDataSource
import com.gsoft.blogapp.databinding.FragmentAgregarFotosBinding
import com.gsoft.blogapp.databinding.FragmentAgregarPropiedadScreenBinding
import com.gsoft.blogapp.domain.propiedades.PropiedadesRepoImpl
import com.gsoft.blogapp.presentation.agregarPropiedad.AgregarPropiedadViewModel
import com.gsoft.blogapp.presentation.propiedades.PropiedadesScreenViewModelFactory


class AgregarFotosFragment : Fragment(R.layout.fragment_agregar_fotos) {

    private val viewModel by activityViewModels<AgregarPropiedadViewModel>{
        PropiedadesScreenViewModelFactory(
                PropiedadesRepoImpl( PropiedadesDataSource())
        )
    }

    private lateinit var binding : FragmentAgregarFotosBinding
    private var listaImagenes : MutableList<Uri> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAgregarFotosBinding.bind(view)
        binding.rvGalleryImages.adapter = PickFotosAdapter(listaImagenes, requireContext())

        binding.bSeleccionarImagenes.setOnClickListener {
            seleccionarImagenes()
        }

        binding.bIrAUbicarEnMapa.setOnClickListener(){
            if (listaImagenes.size > 2 ){
                viewModel.set_uploadImages(listaImagenes)
                findNavController().navigate(R.id.action_agregarFotosFragment_to_buscarEnMapaFragment)
            }else{
                Toast.makeText(activity, "Debes seleccionar al  menos 3 imágenes para continuar", Toast.LENGTH_LONG).show()
            }
        }

        binding.bVolverAAgregarPrincipal.setOnClickListener(){
            findNavController().navigate(R.id.action_agregarFotosFragment_to_agregarPropiedadScreenFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1) {
            if (resultCode === Activity.RESULT_OK) {

                if (data?.clipData  != null) {
                    val count: Int = data?.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        listaImagenes?.add(imageUri)
                    }
                    binding.rvGalleryImages.adapter?.notifyDataSetChanged()
                }
            }
            if (data?.data != null) {
                val imageUri = data.data
                if (imageUri != null) {
                    listaImagenes.add(imageUri)
                }
            }
        }
        binding.rvGalleryImages.adapter?.notifyDataSetChanged()
    }



    private fun seleccionarImagenes() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Pictures: "), 1)
    }
}