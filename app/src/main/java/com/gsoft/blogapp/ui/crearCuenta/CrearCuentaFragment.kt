package com.gsoft.blogapp.ui.crearCuenta

import android.content.ContentValues.TAG
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gsoft.blogapp.R
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.data.datasource.remote.auth.LoginDataSource
import com.gsoft.blogapp.data.models.User
import com.gsoft.blogapp.databinding.FragmentCrearCuentaBinding
import com.gsoft.blogapp.databinding.FragmentLoginScreenBinding
import com.gsoft.blogapp.domain.auth.LoginRepoImpl
import com.gsoft.blogapp.presentation.auth.CrearCuentaScreenViewModel
import com.gsoft.blogapp.presentation.auth.CrearCuentaScreenViewModelFactory
import com.gsoft.blogapp.presentation.auth.LoginScreenViewModel
import com.gsoft.blogapp.presentation.auth.LoginScreenViewModelFactory

class CrearCuentaFragment : Fragment(R.layout.fragment_crear_cuenta) {

    private lateinit var binding : FragmentCrearCuentaBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance()}
    private val viewModel by viewModels<CrearCuentaScreenViewModel>{
        CrearCuentaScreenViewModelFactory(LoginRepoImpl(LoginDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCrearCuentaBinding.bind(view)
        crearCuenta()
    }

    private fun crearCuenta(){
        binding.bSignUp.setOnClickListener {
            val email = binding.tEmail.text.toString().trim()
            val password = binding.tPassword.text.toString().trim()
            val confirmPassword = binding.tPasswordConfirm.text.toString().trim()
            val contacto = binding.tContacto.text.toString().trim()
            validateCredentials(email,password, confirmPassword, contacto)
            signUp(email,password)
        }
    }

    private fun validateCredentials(email: String, password: String, confirmPassword: String, contacto:String) {
        if(email.isEmpty()){
            binding.tEmail.error = "Email está Vacio!"
            return
        }
        if(password.isEmpty()){
            binding.tPassword.error = "Password está Vacio!"
            return
        }

        if(contacto.isEmpty()){
            binding.tContacto.error = "Contacto no puede estar Vacio!"
            return
        }

        if(confirmPassword.isEmpty()){
            binding.tPasswordConfirm.error ="Confirmar contraseña esta Vacio!"
            return
        }

        if (!password.equals(confirmPassword)){
            binding.tPasswordConfirm.error ="Contraseñas no coinciden!"
            binding.tPassword.error = "Contraseñas no coinciden"
            return
        }
    }

    private fun signUp(email: String, password: String) {
        viewModel.crearCuenta(email,password).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBarSignUp.visibility = View.VISIBLE
                    binding.bSignUp.isEnabled = false
                }

                is Result.Success -> {
                    binding.progressBarSignUp.visibility = View.GONE
                    val userId = firebaseAuth.currentUser?.uid
                    val user = User(contacto = binding.tContacto.text.toString(), email = binding.tEmail.text.toString(),
                    id = userId.toString()
                    )
                    FirebaseFirestore.getInstance().collection("users").document(userId.toString())
                            .set(user)
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                    findNavController().navigate(R.id.action_crearCuentaFragment_to_profileScreenFragment)
                    Toast.makeText(
                            requireContext(),
                            "Cuenta creada!  ${result.data?.email}",
                            Toast.LENGTH_LONG
                    ).show()
                }

                is Result.Failure -> {
                    binding.progressBarSignUp.visibility = View.GONE
                    binding.bSignUp.isEnabled = true
                    Toast.makeText(
                            requireContext(),
                            "Error:  ${result.exception}",
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }


}