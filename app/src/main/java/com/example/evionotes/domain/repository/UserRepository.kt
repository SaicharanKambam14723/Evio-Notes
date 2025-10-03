package com.example.evionotes.domain.repository

import com.example.evionotes.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun registerUser(username: String, email: String, password: String): Result<User>
    suspend fun loginUser(email: String, password: String): Result<User>
    suspend fun logoutUser(): Result<Unit>
    fun getLoggedInUser(): Flow<User?>
    suspend fun getCurrentUser(): User?
}