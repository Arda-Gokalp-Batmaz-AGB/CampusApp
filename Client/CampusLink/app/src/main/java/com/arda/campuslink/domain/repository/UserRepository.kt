package com.arda.campuslink.domain.repository

import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    val currentFirebaseUser : FirebaseUser?
    var currentUser : ExtendedUser?
    suspend fun getDetailedUserInfo(userId : String) : Resource<ExtendedUser>
    suspend fun getMinimizedUserInfo(userId : String) : Resource<User>
}