package com.example.firstecommerceproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.AuthUseCases
import com.example.firstecommerceproject.ui.states.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Login screen, managing authentication state and user input.
 *
 * This ViewModel interacts with [AuthUseCases] to handle login, logout, and 
 * session state verification.
 *
 * @property authUseCases The collection of use cases for authentication operations.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())

    /**
     * Observable state representing the current UI state of the Login screen.
     */
    val loginUiState = _loginUiState.asStateFlow()

    /**
     * Updates the email address in the current UI state and validates its format.
     */
    fun onEmailChange(newValue: String) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        val error = if (newValue.isBlank()) {
            null
        } else if (!emailRegex.matches(newValue) && newValue.any { it.isLetter() }) {
            "Invalid email format"
        } else {
            null
        }
        _loginUiState.update { it.copy(email = newValue, emailError = error) }
    }

    /**
     * Updates the password in the current UI state and validates its length.
     */
    fun onPasswordChange(newValue: String) {
        val error = if (newValue.isNotBlank() && newValue.length < 6) {
            "Password must be at least 6 characters"
        } else {
            null
        }
        _loginUiState.update { it.copy(password = newValue, passwordError = error) }
    }

    /**
     * Checks if a user is currently logged into the application.
     * 
     * @return true if an active user session exists, false otherwise.
     */
    fun isUserLoggedIn(): Boolean {
        return authUseCases.getCurrentUser() != null
    }

    /**
     * Triggers the login process using the current email and password in the state.
     */
    fun onLoginClick() {
        val email = _loginUiState.value.email
        val password = _loginUiState.value.password

        if (email.isBlank() || password.isBlank() || _loginUiState.value.emailError != null || _loginUiState.value.passwordError != null) {
            _loginUiState.update { it.copy(errorMessage = "Please fix errors and fill all fields") }
            return
        }

        _loginUiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = authUseCases.login(email, password)
            _loginUiState.update { state ->
                result.fold(
                    onSuccess = {
                        state.copy(isLoading = false, isLoginSuccessful = true)
                    },
                    onFailure = { error ->
                        state.copy(isLoading = false, errorMessage = error.message)
                    }
                )
            }
        }
    }

    /**
     * Triggers the logout process to clear the current user session.
     */
    fun onLogOutClick() {
        viewModelScope.launch {
            val result = authUseCases.logout()
            _loginUiState.update { state ->
                result.fold(
                    onSuccess = {
                        state.copy(logoutSuccess = true, isLoginSuccessful = false)
                    },
                    onFailure = { error ->
                        state.copy(errorMessage = error.message)
                    }
                )
            }
        }
    }

    /**
     * Resets the [LoginUiState.logoutSuccess] flag.
     * 
     * Should be called after the UI has reacted to a successful logout.
     */
    fun resetLogoutState() {
        _loginUiState.update { it.copy(logoutSuccess = false) }
    }
}
