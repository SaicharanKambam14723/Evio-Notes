package com.example.evionotes.domain.use_case.auth

import android.util.Patterns.EMAIL_ADDRESS
import com.example.evionotes.domain.model.User
import com.example.evionotes.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String): Result<User> {

        if(username.isBlank()) {
            return Result.failure(Exception("Username cannot be empty"))
        }
        if(email.isBlank() || !EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(Exception("Email cannot be empty"))
        }
        if(password.length < 6) {
            return Result.failure(Exception("Password must be at least 6 characters long"))
        }

        return userRepository.registerUser(username, email, password)
    }
}