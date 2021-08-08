package com.gsoft.blogapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.domain.profile.ProfileRepo
import com.gsoft.blogapp.domain.propiedades.PropiedadesRepo
import kotlinx.coroutines.Dispatchers

class ProfileScreenViewModel (private val repo: ProfileRepo) : ViewModel() {

    fun logout() = liveData(Dispatchers.IO){
        try{
            emit(repo.logout())
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }

}


class ProfileScreenViewModelFactory(
        private val repo: ProfileRepo,
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProfileRepo::class.java).newInstance(repo)
    }

}