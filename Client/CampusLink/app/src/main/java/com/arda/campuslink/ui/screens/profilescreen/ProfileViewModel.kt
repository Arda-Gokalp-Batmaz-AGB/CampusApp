package com.arda.campuslink.ui.screens.profilescreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arda.campuslink.domain.model.ExtendedUser
import com.arda.campuslink.domain.model.User
import com.arda.campuslink.domain.usecase.AuthenticationUseCase
import com.arda.campuslink.domain.usecase.LoggedUserUseCase
import com.arda.campuslink.ui.auth.AuthUiState
import com.arda.campuslink.util.DebugTags
import com.arda.mainapp.auth.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val authUseCase: AuthenticationUseCase,

) : ViewModel(), LifecycleObserver {
    lateinit var authenticatedUser: User
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    val currentUser: FirebaseUser?
        get() = authUseCase.getCurrentUser()
    init {
        authenticatedUser = loggedUserUseCase.getMinProfileOfCurrentUser()
    }
    fun getUserProfile(user : User) = viewModelScope.launch {
        _uiState.update {
            it.copy(profileFlow = Resource.Loading)
        }
        val result = loggedUserUseCase.getDetailedUserProfile(user.UID)
        _uiState.update {
            it.copy(profileFlow = result)
        }
    }

    fun updateCurrentProfileUser(extendedUser: ExtendedUser)
    {
        _uiState.update {
            it.copy(currentProfileUser = extendedUser)
        }
    }
    init {
        resetEnteredValues()
    }
    fun openEditMode() {
        _uiState.update {
            it.copy(editMode = true)
        }
    }
    fun resetEnteredValues() {
        _uiState.update {
            it.copy(editMode = false )
        }
//        if (!currentUser!!.email.isNullOrEmpty())
//            enteredEmail = currentUser!!.email.toString()
//
//        if (!currentUser!!.displayName.isNullOrEmpty())
//            enteredDisplayName = currentUser!!.displayName.toString()
//
//        if(!currentUser!!.phoneNumber.isNullOrEmpty())
//            enteredPhone = currentUser!!.phoneNumber.toString()
    }

    fun updateProfile() = viewModelScope.launch {
        _uiState.update {
            it.copy(editMode = false )
        }
//        val updatedProfile = buildUpdatedProfile()
//        val result = authService.updateProfile(updatedProfile)
//        _profileFlow.value = result
//        Log.v("REPO", "Update REsult: " + _profileFlow.value)
    }

    fun switchProfileVisibility()  = viewModelScope.launch {
        val user = _uiState.value.currentProfileUser
        if (user != null) {
            loggedUserUseCase.switchProfileVisibility(user)
//            user!!.profilePublic = !user.profilePublic
            _uiState.update {
                it.copy(currentProfileUser = user )
            }
            refreshProfile()
        }

    }
    fun refreshProfile()
    {
        _uiState.update {
            it.copy(isFeedRefreshing = false, signalCompose = !it.signalCompose)
        }
        Log.v(DebugTags.UITag.tag,"Feed Refreshed")
    }
    fun logout() {
        authUseCase.logout()
    }

//    private fun buildUpdatedProfile(): UpdatedUserProfile {
//        return UpdatedUserProfile.Builder()
//            .displayName(enteredDisplayName.let {
//                var result = enteredDisplayName
//                if (currentUser!!.displayName == enteredDisplayName || enteredDisplayName.isNullOrEmpty()) {
//                    result = currentUser!!.displayName.toString()
//                }
//                result
//            })
//            .email(enteredEmail.let {
//                var result = enteredEmail
//                if (currentUser!!.email == enteredEmail || enteredEmail.isNullOrEmpty()) {
//                    result = currentUser!!.email.toString()
//                }
//                result
//            })
//            .userPhoto(photoUri.let {
//                var result = photoUri
//                if (currentUser!!.photoUrl == photoUri || photoUri == null) {
//                    result = currentUser!!.photoUrl
//                }
//                result
//            })
//            .build()
//    }
}