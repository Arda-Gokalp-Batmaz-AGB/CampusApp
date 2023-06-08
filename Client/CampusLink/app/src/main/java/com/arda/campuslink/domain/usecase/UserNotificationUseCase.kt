package com.arda.campuslink.domain.usecase

import com.arda.campuslink.domain.model.ConnectionRequest
import com.arda.campuslink.domain.model.Notification
import com.arda.campuslink.domain.repository.UserRepository
import com.arda.mainapp.auth.Resource
import javax.inject.Inject

class UserNotificationUseCase @Inject constructor(
    private val userRepository: UserRepository,
//    private val userConnectUseCase: UserConnectUseCase
    )
{
    suspend fun acceptUserConnection(notification: Notification): Resource<String> {
        return userRepository.acceptConnectionRequest(notification)
    }
    suspend fun declineUserConnection(notification: Notification): Resource<String> {
        return userRepository.declineConnectionRequest(notification.sender.UID)
    }
    suspend fun getConnectionRequests(userID : String): Resource<ArrayList<Notification>> {
        return userRepository.getNotifications(userID)
    }
}