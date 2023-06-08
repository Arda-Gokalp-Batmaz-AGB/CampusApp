package com.arda.campuslink.domain.usecase

import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.repository.PostRepository
import com.arda.campuslink.domain.repository.UserRepository
import com.arda.mainapp.auth.Resource
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val userRepository: UserRepository,
) {

    suspend fun editUserProfile(updatedUser : ExtendedUser): Resource<ExtendedUser> {
        return userRepository.updateProfile(updatedUserProfile = updatedUser)
    }
}