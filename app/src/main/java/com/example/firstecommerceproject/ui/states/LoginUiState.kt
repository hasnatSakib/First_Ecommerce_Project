package com.example.firstecommerceproject.ui.states

/**
 * Represents the UI state for the Login screen.
 *
 * This data class maintains the input field values and reflects the current state of
 * the authentication process, including success, failure, and loading indications.
 *
 * @property email The email address entered by the user.
 * @property password The password entered by the user.
 * @property isLoading Indicates if an authentication request is currently in progress.
 * @property errorMessage Contains an error message if the login attempt fails.
 * @property isLoginSuccessful Set to true when the user is successfully authenticated.
 * @property logoutSuccess Set to true when the user is successfully logged out.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false,
    val logoutSuccess: Boolean = false
)
