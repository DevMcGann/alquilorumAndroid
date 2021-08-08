package com.gsoft.blogapp.data.datasource.profile

import com.google.firebase.auth.FirebaseAuth

class ProfileDataSource {

    suspend fun logout(){
       FirebaseAuth.getInstance().signOut()
    }




}