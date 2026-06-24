package com.example.autocare.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autocare.data.session.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val sessionManager: SessionManager
) : ViewModel(){

    private val _event = MutableSharedFlow<SplashNav>()
    val event = _event.asSharedFlow()

    init{
        verify()
    }
    fun verify(){
        viewModelScope.launch {
            val isLoggedIn = sessionManager.isLoggedIn.first()
            delay(2000)
            if(isLoggedIn){
                _event.emit(SplashNav.NavigateToHome)
            }else{
                _event.emit(SplashNav.NavigateToSignUp)
            }
        }
    }

    class Factory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
                return SplashScreenViewModel(sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    sealed interface SplashNav{
        object NavigateToHome : SplashNav
        object NavigateToSignUp : SplashNav
    }
}