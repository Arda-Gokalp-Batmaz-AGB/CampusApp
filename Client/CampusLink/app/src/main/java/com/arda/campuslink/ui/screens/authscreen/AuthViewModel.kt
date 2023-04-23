package com.arda.campuslink.ui.auth

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arda.campuslink.domain.usecase.AuthenticationUseCase
import com.arda.mainapp.auth.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase
) : ViewModel(), LifecycleObserver {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUser: FirebaseUser?
        get() = authUseCase.getCurrentUser()

    private val TAG = "REPO"

    init {
        Log.v(TAG, "current USER: " + currentUser.toString())
        if (currentUser != null) {
            _uiState.update {
                it.copy(loginFlow = Resource.Sucess(currentUser!!) )
            }
        }
    }
    fun login() {
        if (emailIsValid() and passwordIsValid()) {
            emailLogin()
        }
    }

    fun register() {
        if (emailIsValid() and passwordIsValid()) {
            emailRegister()
        }
    }
    fun emailLogin() = viewModelScope.launch {
        _uiState.update {
            it.copy(loginFlow = Resource.Loading)
        }
        val result = authUseCase.emailLogin(_uiState.value.enteredEmail, _uiState.value.enteredPassword)
        _uiState.update {
            it.copy(loginFlow = result)
        }
        Log.v(TAG, result.toString())
    }

    fun emailRegister() = viewModelScope.launch {
        _uiState.update {
            it.copy(signUpFlow = Resource.Loading)
        }
        val result = authUseCase.emailRegister(_uiState.value.enteredEmail, _uiState.value.enteredPassword)
        _uiState.update {
            it.copy(signUpFlow = result)
        }
        Log.v(TAG, result.toString())
    }
    fun passwordIsValid(): Boolean {
        _uiState.update {
            it.copy(passwordError = _uiState.value.enteredPassword.length < 6)
        }
        return !_uiState.value.passwordError
    }

    fun emailIsValid(): Boolean {
        _uiState.update {
            it.copy(emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(_uiState.value.enteredEmail).matches())
        }
        return !_uiState.value.emailError
    }
    fun googleLogin(intent : Intent) = viewModelScope.launch {
        _uiState.update {
            it.copy(loginFlow = Resource.Loading)
        }
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        val result = authUseCase.googleLogin(task)
        _uiState.update {
            it.copy(loginFlow = result)
        }
        Log.v(TAG, "Google Login Result = " + result)
    }
    fun changeAuthScreenState(newState: String) {
        _uiState.update {
            it.copy(currentAuthScreenState = newState)
        }
    }
    fun updateEnteredPassword(enteredValue : String)
    {
        _uiState.update {
            it.copy(enteredPassword = enteredValue)
        }
    }
    fun updateEnteredEmail(enteredValue : String)
    {
        _uiState.update {
            it.copy(enteredEmail = enteredValue)
        }
    }
    fun clearState()
    {
        _uiState.update { AuthUiState() }
    }
}