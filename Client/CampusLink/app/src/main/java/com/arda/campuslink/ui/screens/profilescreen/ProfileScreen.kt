package com.arda.campuslink.ui.screens.profilescreen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.ui.components.ProfileTopBar
import com.arda.campuslink.ui.screens.profilescreen.components.EditUserInfo
import com.arda.campuslink.ui.screens.profilescreen.components.OptionsItemStyle
import com.arda.campuslink.ui.screens.profilescreen.components.optionsList
import com.arda.campuslink.ui.screens.profilescreen.components.prepareOptionsData
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.arda.campuslink.R
import com.arda.campuslink.ui.components.FeedItem
import com.arda.campuslink.ui.screens.homescreen.OnBottomReached
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreen(openProfile : MutableState<Boolean>, user: User) {
    val profileViewmodel = hiltViewModel<ProfileViewModel>()
    val state by profileViewmodel.uiState.collectAsState()
    Log.v(DebugTags.UITag.tag,"Current profile view model= ${profileViewmodel}")
//    val openDialog = remember { mutableStateOf(true) }
    AnimatedVisibility(visible = openProfile.value, enter = fadeIn(), exit = fadeOut()) {

        Dialog(
            onDismissRequest = {
                openProfile.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false // experimental
            )
        ) {
            LaunchedEffect(key1 = user.UID)
            {
                Log.v(DebugTags.UITag.tag,"New Profile come")

                profileViewmodel.getUserProfile(user)
            }
            state.profileFlow?.let {
                when (it) {
                    is Resource.Failure<*> -> {
                    }
                    Resource.Loading -> {
                        CircularProgressIndicator()
                    }
                    is Resource.Sucess -> {
                        LaunchedEffect(Unit)
                        {
                            profileViewmodel.updateCurrentProfileUser(it.result)
                        }

                    }
                }
            }

            if (openProfile.value && state.currentProfileUser != null) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    topBar = {
                        ProfileTopBar(openProfile, state.currentProfileUser!!)
                    },
                )
                {
                    profileBody(state,profileViewmodel)
                }
            }

        }
    }
}

@Composable
fun profileBody(state: ProfileUiState, profileViewmodel: ProfileViewModel)
{
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isFeedRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { profileViewmodel.refreshProfile() }) {
        key(state.signalCompose)
        {
            Log.v(DebugTags.UITag.tag,"PROFILE RECOMPOSED")

            AccountInfo(state,profileViewmodel)
        }

    }

}

@Composable
fun AccountInfo(state: ProfileUiState,model: ProfileViewModel)
{

    if(state.editMode)
    {
        EditUserInfo(state,model)
    }
    else
    {
        UserProfile(state,model)
    }
}



@Composable
fun UserProfile(state: ProfileUiState,model : ProfileViewModel,context: Context = LocalContext.current) {

    var listPrepared by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            optionsList.clear()
            prepareOptionsData(state,model,context)
            listPrepared = true
        }
    }

    if (listPrepared) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            item {
                UserDetails(state=state,model = model,context = context)
            }

            items(optionsList) { item ->
                OptionsItemStyle(item = item)
            }

        }
    }
}

@Composable
private fun UserDetails(state: ProfileUiState,model : ProfileViewModel,context: Context) {
    val painter = rememberImagePainter(
        if(state.currentProfileUser?.avatar == null) {
            R.drawable.person
        } else {
            state.currentProfileUser!!.avatar
        }
    )
    var email = model.currentUser!!.email
    var userName = model.currentUser!!.displayName
    var connections = state.currentProfileUser!!.connections
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            modifier = Modifier
                .size(72.dp)
                .clip(shape = CircleShape),
            painter = painter,
            contentDescription = "Your Image",
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {

                if (userName != null) {
                    Text(
                        text =  userName,
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.sailec_bold, FontWeight.Bold)),
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                if (email != null) {
                    Text(
                        text = email,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.sailec_regular, FontWeight.Normal)),
                            color = Color.Gray,
                            letterSpacing = (0.8).sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                    Text(
                        text = "Connections: " + connections.size,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.sailec_regular, FontWeight.Normal)),
                            color = Color.Gray,
                            letterSpacing = (0.8).sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
            }

            IconButton(
                modifier = Modifier
                    .weight(weight = 1f, fill = false),
                onClick = {
                    model.openEditMode()
                }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Details",
                    tint = MaterialTheme.colors.primary
                )
            }

        }
    }
}