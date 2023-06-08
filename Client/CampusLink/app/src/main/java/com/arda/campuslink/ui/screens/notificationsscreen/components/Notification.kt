package com.arda.campuslink.ui.screens.notificationsscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.GppBad
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arda.campuslink.R
import com.arda.campuslink.domain.model.Notification
import com.arda.campuslink.ui.screens.homescreen.HomeViewModel
import com.arda.campuslink.ui.screens.notificationsscreen.NotificationViewModel
import com.arda.campuslink.ui.theme.textColor

@Composable
fun Notification(notification: Notification) {
    val context = LocalContext.current
    var color = colorResource(R.color.darkgreen)

    var textColor = R.color.dark_red2

    val notificationViewmodel = hiltViewModel<NotificationViewModel>()
    val state by notificationViewmodel.uiState.collectAsState()
    val listState = rememberLazyListState()

    Box(modifier = Modifier.background(Color.White))
    {
        Column() {
            Text(text = notification.title)
            Text(text = notification.description)
            IconButton(
                modifier = Modifier
                    .fillMaxSize(),
                onClick = {
//                    model.resetEnteredValues()
                    notificationViewmodel.declineNotification(notification)

                }) {


                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        imageVector = Icons.Outlined.GppBad,
                        contentDescription = "Edit Details",
                        tint = Color.Red
                    )
                    Text(
                        text = "Decline",
                        style = MaterialTheme.typography.subtitle2,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = textColor)
                    )
                }

            }
            IconButton(
                modifier = Modifier
                    .fillMaxSize(),
                onClick = {
                    notificationViewmodel.acceptNotification(notification)
                }) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = "Edit Details",
                        tint = color
                    )
                    Text(
                        text = "Accept",
                        style = MaterialTheme.typography.subtitle2,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = textColor)
                    )
                }

            }
        }
    }
}
