package com.arda.campuslink.ui.screens.profilescreen

import android.net.Uri
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.model.User
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser

data class ProfileUiState(
    var currentProfileUser: ExtendedUser? = null,
    val profileFlow: Resource<ExtendedUser>? = null,
    var editMode : Boolean = false,
    var enteredEmail : String = "",
    var enteredUserName : String = "",
    var enteredSkills : String = "",
    var enteredExperiences : String = "",
    var enteredEducation : String = "",
    var enteredPhotoUri : Uri? = currentProfileUser?.avatar,
    val isFeedRefreshing : Boolean = false,
    var signalCompose : Boolean = false,
    var redirect : Boolean = false,
    )
