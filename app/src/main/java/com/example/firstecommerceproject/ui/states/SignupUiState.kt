package com.example.firstecommerceproject.ui.states

/**
 * Represents the UI state for the Signup screen.
 *
 * This data class maintains the state of the user registration form and reflects
 * the current status of the account creation process.
 *
 * @property username The username entered by the user.
 * @property email The email address entered by the user.
 * @property password The password chosen by the user.
 * @property mobile The mobile number provided by the user.
 * @property isLoading Indicates if a registration request is currently in progress.
 * @property errorMessage Contains an error message if the signup attempt fails.
 * @property isSignupSuccessful Set to true when the account is successfully created.
 */
data class SignupUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val mobile: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignupSuccessful: Boolean = false
)
