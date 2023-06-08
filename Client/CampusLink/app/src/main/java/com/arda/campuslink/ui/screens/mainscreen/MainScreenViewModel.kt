package com.arda.campuslink.ui.screens.mainscreen

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arda.campuslink.domain.usecase.AuthenticationUseCase
import com.arda.campuslink.domain.usecase.LoggedUserUseCase
import com.arda.campuslink.domain.usecase.SearchUseCase
import com.arda.campuslink.domain.usecase.UserConnectUseCase
import com.arda.campuslink.ui.screens.leftbarpopup.LeftBarPopUpUiState
import com.arda.campuslink.util.DebugTags
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val searchUseCase: SearchUseCase,
    private val authUseCase: AuthenticationUseCase
) : ViewModel(), LifecycleObserver {
    private val _uiState = MutableStateFlow(LeftBarPopUpUiState())
    val uiState: StateFlow<LeftBarPopUpUiState> = _uiState.asStateFlow()
    val currentUser: FirebaseUser?
        get() = authUseCase.getCurrentUser()
    init {
        _uiState.update {
            it.copy(currentMinimizedUser = loggedUserUseCase.getMinProfileOfCurrentUser(), currentMinimizedFirebaseUser = currentUser)
        }
    }

    fun getFirebaseUser(): FirebaseUser? {
        return currentUser
    }
    fun performSearch(parameter: String) = viewModelScope.launch {
        val results = searchUseCase.getSearchResults(parameter = parameter)
    }
    fun updateResearchText(parameter: String){
        _uiState.update {
            it.copy(enteredTextParameter = parameter)
        }
        performSearch(parameter = parameter)
    }
}