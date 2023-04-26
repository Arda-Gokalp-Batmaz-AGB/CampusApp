package com.arda.campuslink.ui.screens.profilescreen

import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.model.User
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser

data class ProfileUiState(
    var currentProfileUser: ExtendedUser? = null,
    val profileFlow: Resource<ExtendedUser>? = null,
)
