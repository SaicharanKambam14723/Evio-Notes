package com.example.evionotes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val email: String,
    val passwordHash: String,
    val salt: String,
    val createdAt: Long,
    val isLoggedIn: Boolean = false,
)