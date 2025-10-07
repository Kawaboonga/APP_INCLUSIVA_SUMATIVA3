package com.example.ref01.data

data class User(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val country: String,
    val rut: String? = null     // <- nuevo campo opcional
    )