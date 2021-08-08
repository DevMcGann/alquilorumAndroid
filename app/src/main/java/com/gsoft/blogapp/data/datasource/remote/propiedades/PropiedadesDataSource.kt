package com.gsoft.blogapp.data.datasource.remote.propiedades

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.data.models.Propiedad
import kotlinx.coroutines.tasks.await
import java.util.*

class PropiedadesDataSource {
    suspend fun getPropiedades() : Result<List<Propiedad>> {
        val propList = mutableListOf<Propiedad>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("propiedades").get().await()
        for (propiedad in querySnapshot.documents){
            propiedad.toObject(Propiedad::class.java)?.let{
                propList.add(it)
            }
        }
        return Result.Success(propList)
    }

    fun agregarPropiedad(propiedad: Propiedad){
        val db = FirebaseFirestore.getInstance()
        val prop : MutableMap<String, Any> = mutableMapOf()
        val docID = UUID.randomUUID().toString()
        prop["id"] = docID
        prop["contacto"] = propiedad.contacto!!
        prop["desc"] = propiedad.desc!!
        prop["direccion"] = propiedad.direccion!!
        prop["ownerId"] = propiedad.ownerId!!
        prop["precio"] = propiedad.precio!!
        prop["pago"] = propiedad.pago!!
        prop["lat"] = propiedad.lat!!
        prop["long"] = propiedad.long!!
        prop["imagenes"] = propiedad.imagenes!!

        db.collection("propiedades").document(docID).set(prop)
               .addOnSuccessListener {
                    Log.d("CREADA", "Propiedad Creada ${propiedad.direccion} ${propiedad.imagenes}")
                }
                .addOnFailureListener {
                    Log.d("CREADA", "ERROR!")
                }
    }


    suspend fun getPropiedadesByOwner(id:String) : Result<List<Propiedad>>{
        val propList = mutableListOf<Propiedad>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("propiedades").get().await()
        for (propiedad in querySnapshot.documents){
            propiedad.toObject(Propiedad::class.java)?.let{
                if (it.ownerId == id){
                    propList.add(it)
                }
            }
        }
        return Result.Success(propList)
    }


    fun removePropiedad (id:String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("propiedades").document(id)
                .delete()
                .addOnSuccessListener {
                    Log.d("Eliminada", "Proepiedad eliminada")
                }
                .addOnFailureListener{
                    Log.d("Eliminada", "Error al eliminar Propiedad")
                }
    }

    suspend fun uploadImagesAndGetURL(imgList: MutableList<Uri>) : Result<MutableList<String>>{
        var imgListUrl : MutableList<String> = mutableListOf()
        for (imagen in imgList) {
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/imagenes/$filename")
            ref.putFile(imagen).await()
            ref.downloadUrl.addOnCompleteListener {
                imgListUrl.add(it.result.toString())
                Log.d("DataSource->", "imgURL = $imgListUrl")
            }


        }
        return Result.Success(imgListUrl)
    }


}