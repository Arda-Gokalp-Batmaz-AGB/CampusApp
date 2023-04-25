package com.arda.campuslink.ui.screens.leftbarpopup

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class LeftBarPopUpViewModel @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase
) : ViewModel(), LifecycleObserver {

    private val _uiState = MutableStateFlow(LeftBarPopUpUiState())
    val uiState: StateFlow<LeftBarPopUpUiState> = _uiState.asStateFlow()
    private val TAG = "REPO"
    fun getCurrentUserInfo() {
        Log.v(DebugTags.UITag.tag,"Current Minimized User = ${_uiState.value.currentMinimizedUser}")
        _uiState.update {
            it.copy(currentMinimizedUser = loggedUserUseCase.getMinProfileOfCurrentUser() )
        }
    }
}
