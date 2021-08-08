package com.gsoft.blogapp.domain.auth

import com.google.firebase.auth.FirebaseUser
import com.gsoft.blogapp.data.datasource.remote.auth.LoginDataSource

class LoginRepoImpl (
    private val dataSource: LoginDataSource
        ) : LoginRepo {

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
     return dataSource.SignIn(email,password)
    }

    override suspend fun crearCuenta(email: String, password: String): FirebaseUser? {
        return dataSource.CreateAccount(email,password)
    }

    override fun logout() {
        return dataSource.logout()
    }


}