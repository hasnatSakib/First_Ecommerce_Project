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
        _signupUiState.update { it.copy(firstName = newValue) }
    }

    /**
     * Updates the last name in the current UI state.
     */
    fun onLastNameChange(newValue: String) {
        _signupUiState.update { it.copy(lastName = newValue) }
    }

    /**
     * Updates the email address in the current UI state.
     */
    fun onEmailChange(newValue: String) {
        _signupUiState.update { it.copy(email = newValue) }
    }

    /**
     * Updates the password in the current UI state.
     */
    fun onPasswordChange(newValue: String) {
        _signupUiState.update { it.copy(password = newValue) }
    }

    /**
     * Updates the confirm password in the current UI state.
     */
    fun onConfirmPasswordChange(newValue: String) {
        _signupUiState.update { it.copy(confirmPassword = newValue) }
    }

    /**
     * Updates the mobile number in the current UI state.
     */
    fun onMobileChange(newValue: String) {
        _signupUiState.update { it.copy(mobile = newValue) }
    }

    /**
     * Triggers the signup process using the current input values in the state.
     */
    fun onSignupClick() {
        val firstName = _signupUiState.value.firstName
        val lastName = _signupUiState.value.lastName
        val email = _signupUiState.value.email
        val phone = _signupUiState.value.mobile
        val password = _signupUiState.value.password
        val confirmPassword = _signupUiState.value.confirmPassword

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
