package com.gsoft.blogapp.domain.profile

interface ProfileRepo {
    suspend fun logout()
}