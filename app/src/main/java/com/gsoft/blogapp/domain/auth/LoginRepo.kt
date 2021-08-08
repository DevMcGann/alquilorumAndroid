package com.gsoft.blogapp.domain.auth

import com.google.firebase.auth.FirebaseUser

interface LoginRepo {
    suspend fun signIn (email : String, password : String) : FirebaseUser?
    suspend fun crearCuenta (email:String, password:String) : FirebaseUser?
     fun logout()

}