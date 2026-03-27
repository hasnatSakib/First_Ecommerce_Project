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

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun onEmailChange(newValue: String) {
        _loginUiState.update { it.copy(email = newValue) }
    }

    fun onPasswordChange(newValue: String) {
        _loginUiState.update { it.copy(password = newValue) }
    }

    fun isUserLoggedIn(): Boolean {
        return authUseCases.getCurrentUser() != null
    }

    fun onLoginClick() {
        val email = _loginUiState.value.email
        val password = _loginUiState.value.password

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
}
