package com.arda.campuslink.domain.repository

import com.arda.mainapp.auth.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser : FirebaseUser?
    suspend fun emailLogin(email: String, password: String) : Resource<FirebaseUser>
    suspend fun emailRegister(email: String, password: String)  : Resource<FirebaseUser>
    suspend fun googleLogin(task : Task<GoogleSignInAccount>) : Resource<FirebaseUser>
    fun logout()
}
