package com.gsoft.blogapp.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.gsoft.blogapp.R
import com.gsoft.blogapp.data.datasource.remote.propiedades.PropiedadesDataSource
import com.gsoft.blogapp.data.models.Propiedad
import com.gsoft.blogapp.databinding.FragmentDetailScreenBinding
import com.gsoft.blogapp.domain.propiedades.PropiedadesRepoImpl
import com.gsoft.blogapp.presentation.propiedades.PropiedadesScreenViewModel
import com.gsoft.blogapp.presentation.propiedades.PropiedadesScreenViewModelFactory
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.ImageCarousel


class DetailScreenFragment : Fragment(R.layout.fragment_detail_screen) {

    private val viewModel by activityViewModels<PropiedadesScreenViewModel>{
        PropiedadesScreenViewModelFactory(
            PropiedadesRepoImpl( PropiedadesDataSource())
        )
    }

    private lateinit var binding : FragmentDetailScreenBinding
    private val list = mutableListOf<CarouselItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailScreenBinding.bind(view)
        val carousel: ImageCarousel = binding.carousel
        viewModel.getPropiedadDetail().observe(viewLifecycleOwner, Observer {
            binding.tdirecciondetalle.text = it.direccion
            binding.tcontactodetalle.text = it.contacto
            binding.tdescripciondetalle.text = it.desc
            for (imagen in it.imagenes!!){
                list.add(CarouselItem(imageUrl = imagen,caption = "$"+it.precio.toString()))
            }
            carousel.addData(list)
        })



    }

}