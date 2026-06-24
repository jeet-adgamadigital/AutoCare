package com.example.autocare.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.remote.AuthRepository
import com.example.autocare.data.session.SessionManager
import com.example.autocare.ui.features.splash.SplashScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthScreenViewModel(
    private val authRepository : AuthRepository,
    private val sessionManager : SessionManager
) : ViewModel(){

    private val _uiState = MutableStateFlow<AuthUiStates>(AuthUiStates.SignUp)
    val uiStates = _uiState.asStateFlow()

    private val _apiState = MutableStateFlow<AuthApiState>(AuthApiState.Idle)
    val apiState = _apiState.asStateFlow()

    private val _event = MutableSharedFlow<AuthNav>()
    val event = _event.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _full_name = MutableStateFlow("")
    val full_name = _full_name.asStateFlow()


    fun signUp() {
        _apiState.value = AuthApiState.Loading
        viewModelScope.launch {
            val currentEmail = _email.value.trim()
            val currentPassword = _password.value.trim()
            val currentName = _full_name.value.trim()

            if (currentEmail.isBlank() || currentPassword.isBlank() || currentName.isBlank()) {
                _apiState.value = AuthApiState.Error("Please fill in all details")
                return@launch
            }

            authRepository.signUp(currentEmail, currentPassword, currentName)
                .onSuccess {
                    _apiState.value = AuthApiState.Idle
                    _uiState.value = AuthUiStates.SignIn
                    clearInput()
                }
                .onFailure { exception ->
                    exception.printStackTrace()
                    _apiState.value = AuthApiState.Error(exception.localizedMessage ?: "Try Again")
                }
        }
    }

    fun signIn(){
        viewModelScope.launch {
            val currentEmail = _email.value.trim()
            val currentPassword = _password.value.trim()
            if (currentEmail.isBlank() || currentPassword.isBlank()) {
                _apiState.value = AuthApiState.Error("Please fill in all details")
                return@launch
            }
            authRepository.signIn(currentEmail, currentPassword)
                .onSuccess {
                    sessionManager.setLoginStatus(true)
                    _apiState.value = AuthApiState.Success
                    _event.emit(AuthNav.NavigateToHome)
                }
                .onFailure { exception ->
                    exception.printStackTrace()
                    _apiState.value = AuthApiState.Error(exception.localizedMessage ?: "Try Again")
                }
        }
    }

    fun clearInput(){
        _email.value = ""
        _password.value = ""
        _full_name.value = ""
    }

    fun onMailChange(mail: String) {
        _email.value = mail
    }

    fun onPassChange(pass: String) {
        _password.value = pass
    }

    fun onNameChange(name: String) {
        _full_name.value = name
    }

    class Factory(private val authRepository: AuthRepository,private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthScreenViewModel::class.java)) {
                return AuthScreenViewModel(authRepository,sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


    sealed interface AuthUiStates{
        object SignUp : AuthUiStates
        object SignIn : AuthUiStates
    }

    sealed interface AuthApiState{
        object Success : AuthApiState
        object Loading : AuthApiState
        object Idle : AuthApiState
        data class Error(val message : String) : AuthApiState
    }

    sealed interface AuthNav{
        object NavigateToHome : AuthNav
    }
}