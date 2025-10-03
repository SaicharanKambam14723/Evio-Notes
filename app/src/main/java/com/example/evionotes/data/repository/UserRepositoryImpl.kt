package com.example.evionotes.data.repository

import com.example.evionotes.data.local.dao.UserDAO
import com.example.evionotes.data.local.entity.UserEntity
import com.example.evionotes.domain.model.User
import com.example.evionotes.domain.repository.UserRepository
import com.example.evionotes.util.CryptoUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

sealed class AuthError : Exception() {
    object UserAlreadyExists : AuthError()
    object UsernameAlreadyExists : AuthError()
    object UserNotFound : AuthError()
    object InvalidPassword : AuthError()
}

class UserRepositoryImpl @Inject constructor(
    private val userDAO: UserDAO,
    private val cryptoUtil: CryptoUtil
): UserRepository {

    override suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Result<User> {
        return try {
            if (userDAO.getUserByEmail(email) != null) {
                return Result.failure(AuthError.UserAlreadyExists)
            }

            if(userDAO.getUserByUsername(username) != null) {
                return Result.failure(AuthError.UsernameAlreadyExists)
            }

            val createdAt = System.currentTimeMillis()
            val salt = cryptoUtil.generateSalt()
            val passwordHash = cryptoUtil.hashPassword(password, salt)
            val userId = UUID.randomUUID().toString()

            val userEntity = UserEntity(
                id = userId,
                username = username,
                email = email,
                passwordHash = passwordHash,
                salt = salt,
                createdAt = createdAt,
                isLoggedIn = true
            )

            userDAO.logoutAllUsers()
            userDAO.insertUser(userEntity)

            Result.success(userEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val userEntity = userDAO.getUserByEmail(email)
                ?: return Result.failure(AuthError.UserNotFound)

            val isPasswordValid = cryptoUtil.verifyPassword(
                password,
                userEntity.passwordHash,
                userEntity.salt
            )

            if (!isPasswordValid) {
                return Result.failure(AuthError.InvalidPassword)
            }

            userDAO.logoutAllUsers()
            userDAO.loginUser(userEntity.id)

            Result.success(userEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logoutUser(): Result<Unit> {
        return try {
            userDAO.logoutAllUsers()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getLoggedInUser(): Flow<User?> {
        return userDAO.getLoggedInUserFlow().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getCurrentUser(): User? {
        return userDAO.getLoggedInUser()?.toDomain()
    }
}

// Extension function for mapping
private fun UserEntity.toDomain(): User {
    return User(
        id = id,
        username = username,
        email = email,
        createdAt = createdAt
    )
}
