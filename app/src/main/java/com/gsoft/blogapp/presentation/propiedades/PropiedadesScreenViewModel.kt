package com.gsoft.blogapp.presentation.propiedades

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gsoft.blogapp.R
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.data.models.Propiedad
import com.gsoft.blogapp.domain.auth.LoginRepo
import com.gsoft.blogapp.domain.propiedades.PropiedadesRepo
import kotlinx.coroutines.Dispatchers

class PropiedadesScreenViewModel (private val repo: PropiedadesRepo) : ViewModel() {

    private var propiedadEnDetail = MutableLiveData<Propiedad>()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid


    fun fethPropiedades() = liveData(Dispatchers.IO){
        emit(Result.Loading())
        try{
            emit(repo.getPropiedades())
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }

    fun fetchPropiedadesByOwnerId() = liveData(Dispatchers.IO){
        emit(Result.Loading())
        try{
            emit(userId?.let { repo.getPropiedadesById(it) })
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }


    fun setPropiedadDetail(propiedad: Propiedad) {
        propiedadEnDetail.value = propiedad
    }

    fun getPropiedadDetail() : MutableLiveData<Propiedad> {
        return propiedadEnDetail
    }


     fun deletePropiedad(id:String){
        repo.deletePropiedad(id)
    }

}

class PropiedadesScreenViewModelFactory(
    private val repo: PropiedadesRepo,
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return modelClass.getConstructor(PropiedadesRepo::class.java).newInstance(repo)
    }

}