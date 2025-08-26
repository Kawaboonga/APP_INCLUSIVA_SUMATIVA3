package com.example.ref01.navigation.routes


sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Forgot : Screen("forgot")

    data object First  : Screen("first")

    data object Second : Screen("second")

    data object Third  : Screen("third")

    data object Fourth : Screen("fourth")
}