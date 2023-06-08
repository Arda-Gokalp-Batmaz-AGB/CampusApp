package com.arda.campuslink.ui.screens.notificationsscreen

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arda.campuslink.domain.model.ConnectionRequest
import com.arda.campuslink.domain.model.FeedPost
import com.arda.campuslink.domain.model.Notification
import com.arda.campuslink.domain.usecase.LoggedUserUseCase
import com.arda.campuslink.domain.usecase.UserConnectUseCase
import com.arda.campuslink.domain.usecase.UserNotificationUseCase
import com.arda.campuslink.ui.screens.homescreen.HomeUiState
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val userConnectUseCase: UserConnectUseCase,
    private val userNotificationUseCase: UserNotificationUseCase,
) : ViewModel(), LifecycleObserver {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()
    fun acceptNotification(notification: Notification) = viewModelScope.launch {
        val result = userNotificationUseCase.acceptUserConnection(notification)
        removeNotification(notification)

    }
    fun declineNotification(notification: Notification) = viewModelScope.launch {
        val result = userNotificationUseCase.declineUserConnection(notification)
        removeNotification(notification)
    }
    fun removeNotification(notification: Notification){
        val temp = arrayListOf<Notification>()
        temp.addAll(_uiState.value.currentConnectionNotifications)
        temp.removeIf{it.notificationId == notification.notificationId}
        _uiState.update {
            it.copy(currentConnectionNotifications = temp)
        }
    }
    fun fetchNewRequests() = viewModelScope.launch {
        _uiState.update {
            it.copy(connectionNotificationFlow = Resource.Loading)
        }
        val result = userNotificationUseCase.getConnectionRequests(loggedUserUseCase.getMinProfileOfCurrentUser().UID)
        _uiState.update {
            it.copy(connectionNotificationFlow = result)
        }
    }
    fun updateCurrentNotification(newNotification : ArrayList<Notification>)
    {
        _uiState.update {
            val temp = arrayListOf<Notification>()
            temp.addAll(it.currentConnectionNotifications)
            temp.addAll(newNotification)
            it.copy(currentConnectionNotifications = temp)
        }
        Log.v(DebugTags.UITag.tag,"Current updated feed = ${_uiState.value.currentConnectionNotifications}")
    }
    fun refreshCurrentNotifications()
    {
        resetNotifications()
        fetchNewRequests()
        _uiState.update {
            it.copy(isRequestsRefreshing = false)
        }
        Log.v(DebugTags.UITag.tag,"Connect Requests Refreshed")

    }
    private fun resetNotifications()
    {
        _uiState.update {
            val temp = arrayListOf<Notification>()
            it.copy(currentConnectionNotifications = temp, isRequestsRefreshing = true)
        }
    }
}