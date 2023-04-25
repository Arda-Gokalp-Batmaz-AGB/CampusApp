package com.arda.campuslink.ui.screens.leftbarpopup

import com.arda.campuslink.domain.model.User
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser

data class LeftBarPopUpUiState(
    val currentMinimizedUser: User? = null,
)
