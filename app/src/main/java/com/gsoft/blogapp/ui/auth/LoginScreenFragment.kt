package com.gsoft.blogapp.ui.auth

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
import com.gsoft.blogapp.R
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.data.datasource.remote.auth.LoginDataSource
import com.gsoft.blogapp.databinding.FragmentLoginScreenBinding
import com.gsoft.blogapp.domain.auth.LoginRepoImpl
import com.gsoft.blogapp.presentation.auth.LoginScreenViewModel
import com.gsoft.blogapp.presentation.auth.LoginScreenViewModelFactory


class LoginScreenFragment : Fragment(R.layout.fragment_login_screen) {

    private lateinit var binding : FragmentLoginScreenBinding
    private val firebaseAuth by lazy {FirebaseAuth.getInstance()}
    private val viewModel by viewModels<LoginScreenViewModel>{
        LoginScreenViewModelFactory(LoginRepoImpl(LoginDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginScreenBinding.bind(view)
        isUserLoggedIn()
        doLogin()
        createAccountPage()
    }

    private fun isUserLoggedIn(){
        firebaseAuth.currentUser?.let{
            findNavController().navigate(R.id.action_loginScreenFragment_to_profileScreenFragment)
        }
    }

    private fun doLogin(){
        binding.bLogin.setOnClickListener {
            val email = binding.tEmail.text.toString().trim()
            val password = binding.tPadssword.text.toString().trim()
            validateCredentials(email,password)
            signIn(email,password)
        }
    }

    private fun validateCredentials(email:String, password:String){
        if(email.isEmpty()){
            binding.tEmail.error = "Email está Vacio!"
            return
        }
        if(password.isEmpty()){
            binding.tPadssword.error = "Password está Vacio!"
            return
        }
    }


    private fun signIn(email:String, password:String){
        viewModel.signIn(email,password).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.bLogin.isEnabled = false
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginScreenFragment_to_profileScreenFragment)
                    Toast.makeText(
                            requireContext(),
                            "Sesión iniciada como ${result.data?.email}",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.bLogin.isEnabled = true
                    Log.e("FBAUTH", result.exception.toString())
                    Toast.makeText(
                            requireContext(),
                            "Error:  ${result.exception}",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun createAccountPage(){
        binding.tSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreenFragment_to_crearCuentaFragment)
        }
    }

}