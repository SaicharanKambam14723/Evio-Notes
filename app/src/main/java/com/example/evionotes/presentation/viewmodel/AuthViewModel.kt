package com.example.evionotes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evionotes.domain.model.User
import com.example.evionotes.domain.repository.UserRepository
import com.example.evionotes.domain.use_case.auth.LoginUserUseCase
import com.example.evionotes.domain.use_case.auth.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            userRepository.getLoggedInUser().collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            registerUserUseCase(username, email, password)
                .onSuccess {  user ->
                    _currentUser.value = user
                }
                .onFailure {  exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            loginUserUseCase(email, password)
                .onSuccess {  user ->
                    _currentUser.value = user
                }
                .onFailure {  exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logoutUser()
            _currentUser.value = null
        }
    }

    fun clearError() {
        _error.value = null
    }
}