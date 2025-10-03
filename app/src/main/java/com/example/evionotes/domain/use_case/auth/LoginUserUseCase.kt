package com.example.evionotes.domain.use_case.auth

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.evionotes.domain.model.User
import com.example.evionotes.domain.repository.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if(email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty"))
        }
        if(password.isBlank()) {
            return Result.failure(Exception("Password cannot be empty"))
        }
        return userRepository.loginUser(email, password)
    }
}