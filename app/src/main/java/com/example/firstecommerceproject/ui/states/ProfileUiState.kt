package com.example.firstecommerceproject.ui.states

/**
 * UI State for the Profile screen.
 *
 * @property name The display name of the user.
 * @property email The email address of the user.
 * @property isLoading Indicates if a background operation is in progress.
 * @property errorMessage Contains an error message if an operation fails.
 * @property isLoggedOut Set to true when the user successfully logs out.
 * @property isAccountDeleted Set to true when the user successfully deletes their account.
 */
data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedOut: Boolean = false,
    val isAccountDeleted: Boolean = false
)
