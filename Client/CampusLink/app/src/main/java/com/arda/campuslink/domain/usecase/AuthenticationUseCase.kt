package com.arda.campuslink.domain.usecase

import com.arda.campuslink.domain.repository.AuthRepository
import com.arda.mainapp.auth.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthenticationUseCase @Inject constructor(
    private val authRepository: AuthRepository
)
{

    fun getCurrentUser() : FirebaseUser?
    {
        return authRepository.currentUser
    }

    suspend fun emailLogin(email: String, password: String): Resource<FirebaseUser> {
        return authRepository.emailLogin(email,password)
    }
    suspend fun emailRegister(email: String, password: String) : Resource<FirebaseUser> {
        return authRepository.emailRegister(email,password)
    }
    suspend fun googleLogin(task: Task<GoogleSignInAccount>): Resource<FirebaseUser>
    {
        return authRepository.googleLogin(task)
    }
    fun logout()
    {
        authRepository.logout()
    }
}