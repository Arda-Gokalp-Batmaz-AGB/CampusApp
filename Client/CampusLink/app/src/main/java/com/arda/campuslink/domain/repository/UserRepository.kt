package com.arda.campuslink.domain.repository

import com.arda.campuslink.domain.model.ConnectionRequest
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.model.Notification
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    val currentFirebaseUser : FirebaseUser?
    var currentUser : ExtendedUser?
    suspend fun getDetailedUserInfo(userId : String) : Resource<ExtendedUser>
    suspend fun connectRequestUser(userId : String) : Resource<User>
    suspend fun acceptConnectionRequest(notification: Notification) : Resource<String>
    suspend fun declineConnectionRequest(userId : String) : Resource<String>
    suspend fun getUserConnections(userId : String) : Resource<ArrayList<User>>
    suspend fun getNotifications(userId : String) : Resource<ArrayList<Notification>>
    suspend fun switchProfileVisibility(extendedUser: ExtendedUser) : Resource<ExtendedUser>
    suspend fun updateProfile(updatedUserProfile: ExtendedUser) : Resource<ExtendedUser>
//    suspend fun getMinimizedUserInfo(userId : String) : Resource<User>
}