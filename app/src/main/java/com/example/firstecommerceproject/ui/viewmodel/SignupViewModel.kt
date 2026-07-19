package com.example.firstecommerceproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.AuthUseCases
import com.example.firstecommerceproject.ui.states.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Signup screen, managing user registration state and input.
 *
 * This ViewModel interacts with [AuthUseCases] to create new user accounts.
 *
 * @property authUseCases The collection of use cases for authentication operations.
 */
@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _signupUiState = MutableStateFlow(SignupUiState())

    /**
     * Observable state representing the current UI state of the Signup screen.
     */
    val signupUiState = _signupUiState.asStateFlow()

    /**
     * Updates the first name in the current UI state.
     */
    fun onFirstNameChange(newValue: String) {
        val error = if (newValue.isNotBlank() && newValue.length < 2) "Too short" else null
        _signupUiState.update { it.copy(firstName = newValue, firstNameError = error) }
    }

    /**
     * Updates the last name in the current UI state.
     */
    fun onLastNameChange(newValue: String) {
        val error = if (newValue.isNotBlank() && newValue.length < 2) "Too short" else null
        _signupUiState.update { it.copy(lastName = newValue, lastNameError = error) }
    }

    /**
     * Updates the email address in the current UI state.
     */
    fun onEmailChange(newValue: String) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        val error = if (newValue.isNotBlank() && !emailRegex.matches(newValue)) {
            "Invalid email format"
        } else null
        _signupUiState.update { it.copy(email = newValue, emailError = error) }
    }

    /**
     * Updates the password in the current UI state.
     */
    fun onPasswordChange(newValue: String) {
        val strength = calculatePasswordStrength(newValue)
        val error = if (newValue.isNotBlank() && newValue.length < 6) "Minimum 6 characters" else null
        _signupUiState.update { 
            it.copy(
                password = newValue, 
                passwordError = error,
                passwordStrength = strength.first,
                passwordStrengthLabel = strength.second
            ) 
        }
        // Also re-validate confirm password if it's not empty
        if (_signupUiState.value.confirmPassword.isNotBlank()) {
            onConfirmPasswordChange(_signupUiState.value.confirmPassword)
        }
    }

    private fun calculatePasswordStrength(password: String): Pair<Float, String> {
        if (password.isEmpty()) return 0f to ""
        if (password.length < 6) return 0.2f to "Too weak"
        
        var score = 0f
        if (password.length >= 8) score += 0.2f
        if (password.any { it.isUpperCase() }) score += 0.2f
        if (password.any { it.isDigit() }) score += 0.2f
        if (password.any { !it.isLetterOrDigit() }) score += 0.2f
        
        val label = when {
            score < 0.4f -> "Weak"
            score < 0.7f -> "Medium"
            else -> "Strong"
        }
        return (0.2f + score) to label
    }

    /**
     * Updates the confirm password in the current UI state.
     */
    fun onConfirmPasswordChange(newValue: String) {
        val error = if (newValue.isNotBlank() && newValue != _signupUiState.value.password) {
            "Passwords do not match"
        } else null
        _signupUiState.update { it.copy(confirmPassword = newValue, confirmPasswordError = error) }
    }

    /**
     * Updates the mobile number in the current UI state.
     */
    fun onMobileChange(newValue: String) {
        val error = if (newValue.isNotBlank() && newValue.length < 10) "Invalid phone number" else null
        _signupUiState.update { it.copy(mobile = newValue, mobileError = error) }
    }

    /**
     * Triggers the signup process using the current input values in the state.
     */
    fun onSignupClick() {
        val state = _signupUiState.value
        
        if (state.firstName.isBlank() || state.email.isBlank() || state.password.isBlank() ||
            state.firstNameError != null || state.emailError != null || state.passwordError != null ||
            state.confirmPasswordError != null || state.mobileError != null) {
            _signupUiState.update { it.copy(errorMessage = "Please fix errors and fill all required fields") }
            return
        }

        val firstName = state.firstName
        val lastName = state.lastName
        val email = state.email
        val phone = state.mobile
        val password = state.password
        val confirmPassword = state.confirmPassword

        _signupUiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = authUseCases.signup(firstName, lastName, email, phone, password, confirmPassword)
            _signupUiState.update { state ->
                result.fold(
                    onSuccess = {
                        state.copy(isLoading = false, isSignupSuccessful = true)
                    },
                    onFailure = { error ->
                        state.copy(isLoading = false, errorMessage = error.message)
                    }
                )
            }
        }
    }
}
