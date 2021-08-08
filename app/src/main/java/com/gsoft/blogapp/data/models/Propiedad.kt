package com.gsoft.blogapp.data.models

data class Propiedad(
        val id: String? = "",
        val ownerId: String? = "",
        val contacto: String? = "",
        val desc: String? = "",
        val direccion: String? ="",
        val precio: Int? = 0,
        val lat: String = "",
        val long: String = "",
        val imagenes: MutableList<String>? = null,
        val pago: Boolean = false
        )