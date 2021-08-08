package com.gsoft.blogapp.domain.profile

import com.gsoft.blogapp.data.datasource.profile.ProfileDataSource

class ProfileRepoImpl (private val dataSource : ProfileDataSource): ProfileRepo {

    override suspend fun logout() {
        return dataSource.logout()
    }



}