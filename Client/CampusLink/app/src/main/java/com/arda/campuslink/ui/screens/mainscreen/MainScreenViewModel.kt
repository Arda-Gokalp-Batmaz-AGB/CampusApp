package com.arda.campuslink.ui.screens.mainscreen

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.arda.campuslink.domain.usecase.LoggedUserUseCase
import com.arda.campuslink.ui.screens.leftbarpopup.LeftBarPopUpUiState
import com.arda.campuslink.util.DebugTags
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase
) : ViewModel(), LifecycleObserver {
    private val _uiState = MutableStateFlow(LeftBarPopUpUiState())
    val uiState: StateFlow<LeftBarPopUpUiState> = _uiState.asStateFlow()
    init {
        _uiState.update {
            it.copy(currentMinimizedUser = loggedUserUseCase.getMinProfileOfCurrentUser() )
        }
    }
}