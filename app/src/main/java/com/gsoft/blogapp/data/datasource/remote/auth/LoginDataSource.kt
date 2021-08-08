package com.gsoft.blogapp.data.datasource.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await




class LoginDataSource {

    suspend fun  SignIn (email:String, password: String) : FirebaseUser? {
        val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).await()
        return authResult.user
    }

    suspend fun CreateAccount (email:String, password:String) : FirebaseUser? {
        val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).await()
        return authResult.user
    }


    fun logout(){
        FirebaseAuth.getInstance().signOut()
    }


    
}