package com.example.evionotes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evionotes.domain.model.User
import com.example.evionotes.domain.repository.UserRepository
import com.example.evionotes.domain.use_case.auth.LoginUserUseCase
import com.example.evionotes.domain.use_case.auth.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val currentUser: StateFlow<User?> = authState
        .map { state -> if (state is AuthState.LoggedIn) state.user else null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            userRepository.getLoggedInUser().collect { user ->
                _authState.value = if (user != null) AuthState.LoggedIn(user) else AuthState.LoggedOut
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            registerUserUseCase(username, email, password)
                .onSuccess {
                    // AuthState will automatically update via observeUser
                }
                .onFailure { ex ->
                    _error.value = ex.message
                }

            _isLoading.value = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            loginUserUseCase(email, password)
                .onSuccess {
                    // AuthState will automatically update via observeUser
                }
                .onFailure { ex ->
                    _error.value = ex.message
                }

            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logoutUser()
        }
    }

    fun clearError() {
        _error.value = null
    }
}
