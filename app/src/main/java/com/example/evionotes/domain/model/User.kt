package com.example.evionotes.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val createdAt: Long
)