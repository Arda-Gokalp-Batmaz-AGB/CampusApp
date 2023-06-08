package com.arda.campuslink.ui.screens.notificationsscreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.arda.campuslink.ui.components.FeedItem
import com.arda.campuslink.ui.screens.homescreen.HomeViewModel
import com.arda.campuslink.ui.screens.homescreen.OnBottomReached
import com.arda.campuslink.ui.screens.notificationsscreen.components.Notification
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen() {
    val notificationViewModel = hiltViewModel<NotificationViewModel>()
    val state by notificationViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    Box(
        Modifier

    ) {
        LaunchedEffect(Unit){
            notificationViewModel.fetchNewRequests()

        }
        state.connectionNotificationFlow?.let {
            when (it) {
                is Resource.Failure<*> -> {
                }
                Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Sucess -> {
                    LaunchedEffect(Unit)
                    {
                        Log.v(DebugTags.UITag.tag, "Notifies Fetched Sucessfully!!")
                        notificationViewModel.updateCurrentNotification(it.result)
                    }

                }
            }
        }


        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRequestsRefreshing)

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { notificationViewModel.refreshCurrentNotifications() }) {
            LazyColumn(
                modifier = Modifier,
                state = listState
            )
            {
                items(state.currentConnectionNotifications.size) { idx ->
                    Log.v(DebugTags.UITag.tag, "current notification ${state.currentConnectionNotifications[idx]}")
                    Notification(notification = state.currentConnectionNotifications[idx])
                }

            }
//            listState.OnBottomReached(buffer = 3) {
//                notificationViewModel.fetchNewRequests()
//            }
        }
    }
}