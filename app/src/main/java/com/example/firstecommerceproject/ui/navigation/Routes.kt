package com.example.firstecommerceproject.ui.navigation

sealed class Routes(
    val route: String
) {
    data object AuthScreen : Routes("auth")
    data object LoginScreen : Routes("login")
    data object SignUpScreen : Routes("signup")
    data object HomeScreen : Routes("home")
}