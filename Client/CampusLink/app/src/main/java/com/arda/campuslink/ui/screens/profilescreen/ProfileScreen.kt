package com.arda.campuslink.ui.screens.profilescreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.ui.components.ProfileTopBar
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource

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
                    profileBody(state.currentProfileUser!!)
                }
            }

        }
    }
}

@Composable
fun profileBody(currentUser : ExtendedUser)
{

}