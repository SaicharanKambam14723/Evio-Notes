package com.example.evionotes.presentation.viewmodel

import com.example.evionotes.domain.model.User

sealed class AuthState {
    object Loading : AuthState()
    data class LoggedIn(val user: User) : AuthState()
    object LoggedOut : AuthState()
}