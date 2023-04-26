package com.arda.campuslink.domain.usecase

import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.repository.UserRepository
import com.arda.campuslink.util.ImageProcessUtils
import com.arda.mainapp.auth.Resource
import javax.inject.Inject


class LoggedUserUseCase @Inject constructor(
    private val userRepository: UserRepository,

    ) {
    suspend fun getDetailedUserProfile(userId: String): Resource<ExtendedUser> {
        return userRepository.getDetailedUserInfo(userId)
    }

    fun getMinProfileOfCurrentUser(): User {
        return User(
            userRepository.currentFirebaseUser!!.uid,
            userRepository.currentFirebaseUser!!.displayName!!,
            "",
            userRepository.currentFirebaseUser!!.photoUrl!!
        )
    }

    suspend fun getMinimizedUserProfile(userId: String): Resource<User> {
        return userRepository.getMinimizedUserInfo(userId)
    }
}