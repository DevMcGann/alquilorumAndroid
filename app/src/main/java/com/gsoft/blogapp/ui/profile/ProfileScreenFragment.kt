package com.gsoft.blogapp.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.gsoft.blogapp.R
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.data.datasource.profile.ProfileDataSource
import com.gsoft.blogapp.data.datasource.remote.propiedades.PropiedadesDataSource
import com.gsoft.blogapp.data.models.Propiedad
import com.gsoft.blogapp.databinding.FragmentProfileScreenBinding
import com.gsoft.blogapp.databinding.FragmentPropiedadesScreenBinding
import com.gsoft.blogapp.domain.profile.ProfileRepoImpl
import com.gsoft.blogapp.domain.propiedades.PropiedadesRepoImpl
import com.gsoft.blogapp.presentation.profile.ProfileScreenViewModel
import com.gsoft.blogapp.presentation.profile.ProfileScreenViewModelFactory
import com.gsoft.blogapp.presentation.propiedades.PropiedadesScreenViewModel
import com.gsoft.blogapp.presentation.propiedades.PropiedadesScreenViewModelFactory
import com.gsoft.blogapp.ui.propiedades.Adapter.PropiedadesScreenAdapter


class ProfileScreenFragment : Fragment(R.layout.fragment_profile_screen),PropiedadesScreenAdapter.OnPropiedadClickListener {

    private lateinit var binding : FragmentProfileScreenBinding
    private lateinit var mainAdapter: PropiedadesScreenAdapter
    private var lista_Propiedades : MutableList<Propiedad> = mutableListOf()

    private val viewModel by viewModels<PropiedadesScreenViewModel>{
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

        binding = FragmentProfileScreenBinding.bind(view)
        binding.labelEmail.text = FirebaseAuth.getInstance().currentUser?.email
        binding.rvMisPropiedades.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMisPropiedades.adapter = mainAdapter

        viewModel.fetchPropiedadesByOwnerId().observe(viewLifecycleOwner, Observer { result ->
            when (result) {

                is Result.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressbar.visibility = View.GONE
                    lista_Propiedades = result.data as MutableList<Propiedad>
                    mainAdapter.setListaDePropiedades(lista_Propiedades)
                }

                is Result.Failure -> {
                    Log.e("ERRORLOCO", result.exception.toString())
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(
                            requireContext(),
                            "Hubo un error : ${result.exception}",
                            Toast.LENGTH_LONG
                    ).show()

                }
            }
        })

        goBackToHome()
        goToAddNewProp()
        logout()
    }

    private fun goBackToHome(){
        binding.bHome.setOnClickListener{
            findNavController().navigate(R.id.action_profileScreenFragment_to_homeScreenFragment)
        }
    }

    private fun logout(){
        binding.bCerrarSesion.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_profileScreenFragment_to_homeScreenFragment)
        }
    }

    private fun goToAddNewProp(){
        binding.bAgregarPropiedad.setOnClickListener{
            findNavController().navigate(R.id.action_profileScreenFragment_to_agregarPropiedadScreenFragment)
        }
    }

    override  fun onPropiedadClick(propiedad: Propiedad, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Eliminar")
        builder.setMessage("Eliminar Propiedad?")
        builder.setPositiveButton("Si"){dialog, which ->
            propiedad.id?.let { viewModel.deletePropiedad(it) }
            lista_Propiedades.removeAt(position)
            binding.rvMisPropiedades.adapter?.notifyItemRemoved(position)
            binding.rvMisPropiedades.adapter?.notifyDataSetChanged()
            Log.d("PROPIEDADBORRAR", propiedad.toString())
        }

        builder.setNegativeButton("No"){dialog,which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
}


