package com.example.firstecommerceproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.AuthUseCases
import com.example.firstecommerceproject.domain.use_case.DataUseCases
import com.example.firstecommerceproject.ui.states.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen, responsible for fetching and managing dashboard data.
 *
 * It coordinates with [DataUseCases] to retrieve user profile information, 
 * promotional banners, and product categories from the backend.
 *
 * @property dataUseCases The collection of use cases for data operations.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataUseCases: DataUseCases,
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())

    /**
     * Observable state representing the current UI state of the Home screen.
     */
    val homeUiState = _homeUiState.asStateFlow()

    init {
        getHomeData()
    }

    /**
     * Fetches all necessary data for the home dashboard.
     * 
     * This includes the user's name, active promotional banners, and 
     * available product categories. Updates [homeUiState] with the results.
     */
    fun getHomeData() {
        _homeUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val nameResult = authUseCases.getName()
            val bannersResult = dataUseCases.getBanners()
            val categoriesResult = dataUseCases.getCategories()

            _homeUiState.update { state ->
                state.copy(
                    isLoading = false,
                    name = nameResult.getOrNull(),
                    banners = bannersResult.getOrNull() ?: emptyList(),
                    categories = categoriesResult.getOrNull() ?: emptyList(),
                    errorMessage = (nameResult.exceptionOrNull()
                        ?: bannersResult.exceptionOrNull()
                        ?: categoriesResult.exceptionOrNull())?.message
                )
            }
        }
    }
}
