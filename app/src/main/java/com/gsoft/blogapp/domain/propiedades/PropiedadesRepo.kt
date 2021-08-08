package com.gsoft.blogapp.domain.propiedades

import android.net.Uri
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.data.models.Propiedad

interface PropiedadesRepo {
    suspend fun getPropiedades() : Result<List<Propiedad>>
    suspend fun getPropiedadesById(id:String) : Result<List<Propiedad>>
    fun crearPropiedad(propiedad: Propiedad)
    fun deletePropiedad(id:String)
    suspend fun uploadImagesAndGetURL(imgList:MutableList<Uri>) : Result<MutableList<String>>
}