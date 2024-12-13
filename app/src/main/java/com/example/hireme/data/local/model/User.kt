package com.example.hireme.data.local.model

data class User(
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)