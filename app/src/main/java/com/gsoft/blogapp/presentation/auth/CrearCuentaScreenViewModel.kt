package com.gsoft.blogapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.domain.auth.LoginRepo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class CrearCuentaScreenViewModel(
        private val repo: LoginRepo
) : ViewModel(){

    fun crearCuenta(email:String, password:String) = liveData(Dispatchers.IO){
        emit(Result.Loading())
        try{
            emit(Result.Success(repo.crearCuenta(email,password)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }
}

class CrearCuentaScreenViewModelFactory(private val repo:LoginRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CrearCuentaScreenViewModel(repo) as T
    }


}