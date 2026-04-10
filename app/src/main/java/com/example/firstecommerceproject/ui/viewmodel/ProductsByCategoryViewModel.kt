package com.example.firstecommerceproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.DataUseCases
import com.example.firstecommerceproject.ui.states.ProductsByCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for displaying products filtered by a specific category.
 *
 * This ViewModel manages the state and logic for the [CategoryProductScreen].
 * It uses [DataUseCases] to fetch the list of products from the repository.
 *
 * @property dataUseCases The collection of data-related use cases.
 */
@HiltViewModel
class ProductsByCategoryViewModel @Inject constructor(
    private val dataUseCases: DataUseCases
) : ViewModel() {

    private val _productsByCategoryUiState = MutableStateFlow(ProductsByCategoryUiState())

    /**
     * Observable state for the products by category screen.
     */
    val productsByCategoryUiState = _productsByCategoryUiState.asStateFlow()

    /**
     * Fetches products belonging to the specified [category] and updates [uiState].
     *
     * @param category The name or ID of the category to filter by.
     */
    fun getProductsByCategory(category: String) {
        _productsByCategoryUiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = dataUseCases.getProductsByCategory(category)
            _productsByCategoryUiState.update { state ->
                state.copy(
                    isLoading = false,
                    products = result.getOrNull() ?: emptyList(),
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }
}
