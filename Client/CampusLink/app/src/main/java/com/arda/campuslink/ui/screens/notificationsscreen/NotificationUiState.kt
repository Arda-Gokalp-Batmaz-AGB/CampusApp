package com.arda.campuslink.ui.screens.notificationsscreen

import com.arda.campuslink.domain.model.ConnectionRequest
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.Notification
import com.arda.mainapp.auth.Resource

data class NotificationUiState (
    val isRequestsRefreshing : Boolean = false,
    val connectionNotificationFlow: Resource<ArrayList<Notification>>? = null,
    val currentConnectionNotifications: ArrayList<Notification> = arrayListOf(),
    )