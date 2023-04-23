package com.arda.campuslink.ui.auth

import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser

data class AuthUiState(
    val currentAuthScreenState : String = "Login",
    val enteredPassword: String = "",
    val enteredEmail: String = "",
    val passwordError: Boolean = false,
    val emailError: Boolean = false,
    var submitButtonOn: Boolean = true,
    var invalidUserError: Boolean = false,
    val loginFlow: Resource<FirebaseUser>? = null,
    val signUpFlow: Resource<FirebaseUser>? = null
)
