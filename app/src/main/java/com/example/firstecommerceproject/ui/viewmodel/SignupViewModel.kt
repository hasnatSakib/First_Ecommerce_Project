package com.example.firstecommerceproject.ui.viewmodel

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.AuthUseCases
import com.example.firstecommerceproject.ui.states.SignupUiState
import com.example.firstecommerceproject.ui.util.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _signupUiState = MutableStateFlow(SignupUiState())
    val signupUiState = _signupUiState.asStateFlow()

    fun onUsernameChange(newValue: String) {
        _signupUiState.update { it.copy(username = newValue) }
    }

    fun onEmailChange(newValue: String) {
        _signupUiState.update { it.copy(email = newValue) }
    }

    fun onPasswordChange(newValue: String) {
        _signupUiState.update { it.copy(password = newValue) }
    }

    fun onMobileChange(newValue: String) {
        _signupUiState.update { it.copy(mobile = newValue) }
    }

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
