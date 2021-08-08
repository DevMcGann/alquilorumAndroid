package com.gsoft.blogapp.ui.propiedades

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gsoft.blogapp.R
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.data.datasource.remote.auth.LoginDataSource
import com.gsoft.blogapp.data.datasource.remote.propiedades.PropiedadesDataSource
import com.gsoft.blogapp.data.models.Propiedad
import com.gsoft.blogapp.databinding.FragmentPropiedadesScreenBinding
import com.gsoft.blogapp.domain.auth.LoginRepoImpl
import com.gsoft.blogapp.domain.propiedades.PropiedadesRepoImpl
import com.gsoft.blogapp.presentation.propiedades.PropiedadesScreenViewModel
import com.gsoft.blogapp.presentation.propiedades.PropiedadesScreenViewModelFactory
import com.gsoft.blogapp.ui.propiedades.Adapter.PropiedadesScreenAdapter


class PropiedadesScreenFragment : Fragment(R.layout.fragment_propiedades_screen),PropiedadesScreenAdapter.OnPropiedadClickListener {

    private lateinit var mainAdapter: PropiedadesScreenAdapter
    private val viewModel by activityViewModels<PropiedadesScreenViewModel>{
        PropiedadesScreenViewModelFactory(
            PropiedadesRepoImpl( PropiedadesDataSource())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainAdapter = PropiedadesScreenAdapter(requireContext(), this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPropiedadesScreenBinding.bind(view)

        binding.rvPropiedades.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPropiedades.adapter = mainAdapter

        viewModel.fethPropiedades().observe(viewLifecycleOwner, Observer { result ->
            when (result) {

                is Result.Loading -> {
                    binding.progressBarProp.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBarProp.visibility = View.GONE
                    mainAdapter.setListaDePropiedades(result.data)
                }

                is Result.Failure -> {
                    Log.e("ERRORLOCO", result.exception.toString())
                    binding.progressBarProp.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Hubo un error : ${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        })


    } //ovc

    override  fun onPropiedadClick(propiedad: Propiedad, position: Int) {
        viewModel.setPropiedadDetail(propiedad)
        Log.d("SETPROP", propiedad.toString())
        findNavController().navigate(R.id.action_propiedadesScreenFragment_to_detailScreenFragment)
    }

}