package com.example.ref01.navigation.routes

sealed class Screen(val route: String) {
    data object Login   : Screen("login")
    data object Register: Screen("register")
    data object Forgot  : Screen("forgot")
    data object First   : Screen("first")
    data object Second  : Screen("second")
    data object Third   : Screen("third")
    data object Fourth  : Screen("fourth")

    // HOME (catálogo)
    data object Home    : Screen("home")

    // DETALLE (con argumento)
    data object BookDetail : Screen("bookDetail/{bookId}") {
        const val ARG_BOOK_ID = "bookId"
        fun createRoute(bookId: Int) = "bookDetail/$bookId"
    }

    // NUEVAS RUTAS
    data object Settings      : Screen("settings")
    data object DeviceFinder  : Screen("buscar_dispositivo")

    data object UserProfile : Screen("user_profile")
}
