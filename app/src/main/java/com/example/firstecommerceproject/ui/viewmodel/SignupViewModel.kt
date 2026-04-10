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
     * Updates the username in the current UI state.
     */
    fun onUsernameChange(newValue: String) {
        _signupUiState.update { it.copy(username = newValue) }
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
     * Updates the mobile number in the current UI state.
     */
    fun onMobileChange(newValue: String) {
        _signupUiState.update { it.copy(mobile = newValue) }
    }

    /**
     * Triggers the signup process using the current input values in the state.
     */
    fun onSignupClick() {
        val name = _signupUiState.value.username
        val email = _signupUiState.value.email
        val password = _signupUiState.value.password

        _signupUiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = authUseCases.signup(name, email, password)
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
