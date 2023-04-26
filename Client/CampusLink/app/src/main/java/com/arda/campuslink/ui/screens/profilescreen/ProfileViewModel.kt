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
    private val loggedUserUseCase: LoggedUserUseCase
) : ViewModel(), LifecycleObserver {
    lateinit var authenticatedUser: User
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

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
//    fun logout() {
//        authenticationUseCase.logout()
//    }
}