package com.gsoft.blogapp.domain.propiedades

import android.net.Uri
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.data.datasource.remote.propiedades.PropiedadesDataSource
import com.gsoft.blogapp.data.models.Propiedad

class PropiedadesRepoImpl (private val dataSource : PropiedadesDataSource): PropiedadesRepo {
    override suspend fun getPropiedades(): Result<List<Propiedad>> {
        return dataSource.getPropiedades()
    }

    override suspend fun getPropiedadesById(id: String): Result<List<Propiedad>> {
        return dataSource.getPropiedadesByOwner(id)
    }

    override  fun crearPropiedad(propiedad: Propiedad) {
        return dataSource.agregarPropiedad(propiedad)
    }

    override  fun deletePropiedad(id: String) {
        return dataSource.removePropiedad(id)
    }

    override suspend fun uploadImagesAndGetURL(imgList: MutableList<Uri>): Result<MutableList<String>> {
        return dataSource.uploadImagesAndGetURL(imgList)
    }


}