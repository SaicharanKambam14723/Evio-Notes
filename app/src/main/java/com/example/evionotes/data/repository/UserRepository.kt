package com.example.evionotes.data.repository

import com.example.evionotes.data.local.UserDAO
import com.example.evionotes.data.local.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDAO
) {
    suspend fun upsertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserById(userId: Int): UserEntity? {
        return userDao.getUserById(userId)
    }

    suspend fun getUserByUserName(username: String): UserEntity? {
        return userDao.getUserByUserName(username)
    }

    suspend fun deleteUserById(userId: Int) {
        userDao.deleteUserById(userId)
    }
}