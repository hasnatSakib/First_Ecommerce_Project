package com.example.firstecommerceproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.DataUseCases
import com.example.firstecommerceproject.ui.states.ProductDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Product Details screen.
 *
 * This ViewModel handles the logic for observing specific product details from the
 * data layer and exposing them as a lifecycle-aware [ProductDetailsUiState] flow.
 *
 * @property dataUseCases The entry point to domain layer logic.
 */
@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val dataUseCases: DataUseCases
) : ViewModel() {

    // Internal state flow for UI updates
    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    
    /**
     * Publicly exposed UI state that can be observed by the view.
     */
    val uiState = _uiState.asStateFlow()

    /**
     * Starts observing detailed information for a product by its [productId].
     *
     * Updates [uiState] to reflect loading, success, or error states in real-time.
     *
     * @param productId The ID of the product to fetch.
     */
    fun getProductDetails(productId: String) {
        // Reset state to loading before starting the observation
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            dataUseCases.observeProductById(productId).collectLatest { result ->
                result.onSuccess { product ->
                    _uiState.update { 
                        it.copy(product = product, isLoading = false) 
                    }
                }.onFailure { error ->
                    _uiState.update { 
                        it.copy(errorMessage = error.message ?: "Failed to load product details", isLoading = false) 
                    }
                }
            }
        }
    }
}
