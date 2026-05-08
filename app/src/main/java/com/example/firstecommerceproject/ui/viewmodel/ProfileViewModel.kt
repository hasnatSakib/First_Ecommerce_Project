package com.example.firstecommerceproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.AuthUseCases
import com.example.firstecommerceproject.ui.states.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Profile screen.
 *
 * Manages user profile data and authentication actions like logout and account deletion.
 *
 * @property authUseCases Collection of authentication-related use cases.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        _profileUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val nameResult = authUseCases.getName()
            val user = authUseCases.getCurrentUser()
            _profileUiState.update { state ->
                state.copy(
                    isLoading = false,
                    name = nameResult.getOrNull() ?: "User",
                    email = user?.email ?: ""
                )
            }
        }
    }

    /**
     * Logs the current user out of the application.
     */
    fun onLogoutClick() {
        _profileUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authUseCases.logout()
            _profileUiState.update { state ->
                result.fold(
                    onSuccess = { state.copy(isLoading = false, isLoggedOut = true) },
                    onFailure = { state.copy(isLoading = false, errorMessage = it.message) }
                )
            }
        }
    }

    /**
     * Deletes the current user's account and data.
     */
    fun onDeleteAccountClick() {
        _profileUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authUseCases.deleteAccount()
            _profileUiState.update { state ->
                result.fold(
                    onSuccess = { state.copy(isLoading = false, isAccountDeleted = true) },
                    onFailure = { state.copy(isLoading = false, errorMessage = it.message) }
                )
            }
        }
    }
}
