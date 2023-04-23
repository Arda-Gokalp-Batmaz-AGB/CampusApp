package com.arda.campuslink.ui.screens.profilescreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arda.campuslink.domain.usecase.AuthenticationUseCase
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase
) : ViewModel(), LifecycleObserver {
    val currentUser: FirebaseUser?
        get() = authenticationUseCase.getCurrentUser()
    var editMode by mutableStateOf(false)
    var enteredDisplayName by mutableStateOf("")
    var enteredEmail by mutableStateOf("")
    var enteredPhone by mutableStateOf("")
    var photoUri by mutableStateOf(currentUser?.photoUrl)

    private val _profileFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val profileFlow: StateFlow<Resource<FirebaseUser>?> = _profileFlow

    init {
        resetEnteredValues()
    }

    fun resetEnteredValues() {
        if (!currentUser?.email.isNullOrEmpty())
            enteredEmail = currentUser!!.email.toString()

        if (!currentUser?.displayName.isNullOrEmpty())
            enteredDisplayName = currentUser!!.displayName.toString()

        if(!currentUser?.phoneNumber.isNullOrEmpty())
            enteredPhone = currentUser!!.phoneNumber.toString()
    }
    fun logout() {
        authenticationUseCase.logout()
    }
}