package com.example.model

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val points: Int = 0,
    val referralCode: String = "",
    val lastCheckIn: Long = 0L,
    val isAdmin: Boolean = false
)

data class Transaction(
    val id: String = "",
    val title: String = "",
    val amount: Int = 0,
    val timestamp: Long = 0L
)
