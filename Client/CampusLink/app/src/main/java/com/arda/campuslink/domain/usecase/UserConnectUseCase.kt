package com.arda.campuslink.domain.usecase

import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.repository.UserRepository
import com.arda.mainapp.auth.Resource
import javax.inject.Inject

class UserConnectUseCase @Inject constructor(
    private val userRepository: UserRepository,

    )
    {
        suspend fun connectUser(userId : String) : Resource<User>{
            return userRepository.connectRequestUser(userId)
        }
//        suspend fun removeConnectUser(userId : String) : Resource<String>{
//            return userRepository.removeConnectUser(userId)
//        }
        suspend fun getUserConnections(userId : String) : Resource<ArrayList<User>>{
            return userRepository.getUserConnections(userId)
        }
    }