package com.example.firstecommerceproject.ui.states

data class SignupUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val mobile: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignupSuccessful: Boolean = false
)
