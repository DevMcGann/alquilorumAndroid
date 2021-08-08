package com.gsoft.blogapp.presentation.agregarPropiedad

/*
* 1 - Pagar
* 2- asignar nombres a las imagenes (uuid + time.now + .jpg)
* 3 - subir las imagenes al storage
* 4- armar el objeto propiedad con los datos y la lista de imagenes URL
* 5 - subir obj a firestore
* */

import android.net.Uri
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gsoft.blogapp.R
import com.gsoft.blogapp.core.Result
import com.gsoft.blogapp.data.models.Propiedad
import com.gsoft.blogapp.domain.propiedades.PropiedadesRepo
import kotlinx.coroutines.Dispatchers
import java.util.*

class AgregarPropiedadViewModel(private val repo: PropiedadesRepo) : ViewModel() {

    private var direccion: String = ""
    private var precio = 0
    private var desc = ""
    private var lat = ""
    private var long = ""
    private var contacto = ""
    private var pago = false
    private var ownerId = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var imagenesUri: MutableList<Uri> = mutableListOf()
    private var imagenes: MutableList<String> = mutableListOf()


    fun setDireccionPrecioDescOwnerId(dir: String, prec: Int, descr: String, own: String, contact: String) {
        direccion = dir
        precio = prec
        desc = descr
        ownerId = own
        contacto = contact
        Log.d("primerPaso", "dir ${direccion} -- precio ${precio} -- descr ${desc} ---owner ${ownerId}")
    }

    fun setLatLong(latitud: String, longitud: String) {
        lat = latitud
        long = longitud
    }

    fun setImagenes(urlList:MutableList<String>){
        imagenes = urlList
    }

    fun getImagenes() : MutableList<String>{
        return imagenes
    }

    fun setPago() {
        pago = true
    }

    fun set_uploadImages(imgList: MutableList<Uri>) {
        imagenesUri = imgList
        Log.d("URILISTA", imagenesUri.toString())
    }

    fun uploadImagesToStorageAndGetURL() = liveData(Dispatchers.IO){
        emit(Result.Loading())
        try{
            Log.d("IMAGENESURI", imagenesUri.toString())
            emit(repo.uploadImagesAndGetURL(imagenesUri))
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }

   /* fun uploadImagesToStorage() {

        for (imagen in imagenesUri) {
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/imagenes/$filename")
            ref.putFile(imagen)
                    .addOnCompleteListener() {
                        ref.downloadUrl.addOnSuccessListener {
                            imagenes.add(it.toString())
                            Log.d("IMAGENES", "imagenes tiene -> ${imagenes.toString()}")

                        }
                                .addOnFailureListener {
                                    Log.d("DownloadURL", "Error con el DownloadURL")
                                }
                    }
                    .addOnFailureListener {
                        Log.d("UploadImage", "Error subiendo la imagen")
                    }
        }
        Log.d("FINCARGA", "imagenes al finalizar el bucle es ${get_imagenesURL()}")
        finalizarUpload()
    }*/


    fun finalizarUpload() {
        val propiedad = Propiedad(
                direccion = direccion,
                desc = desc,
                precio = precio,
                contacto = contacto,
                ownerId = ownerId,
                lat = lat,
                long = long,
                pago = true,
                imagenes = imagenes
        )

        agregarPropiedad(propiedad)
    }


    fun agregarPropiedad(propiedad: Propiedad) {
        repo.crearPropiedad(propiedad)
    }

}