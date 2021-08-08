package com.gsoft.blogapp.data.models

data class User(
    val id : String,
    val email : String,
    val contacto : String,
    val propiedades : List<String>? = null
)