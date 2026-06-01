package com.example.firstecommerceproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.DataUseCases
import com.example.firstecommerceproject.ui.states.ProductDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Product Details screen.
 */
@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val dataUseCases: DataUseCases
) : ViewModel() {

    private val _productDetailsUiState = MutableStateFlow(ProductDetailsUiState())
    val productDetailsUiState = _productDetailsUiState.asStateFlow()

    fun getProductDetails(productId: String) {
        _productDetailsUiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            val result = dataUseCases.getProductWithVariants(productId)
            
            result.onSuccess { pair ->
                val (product, variants) = pair
                _productDetailsUiState.update { 
                    it.copy(
                        product = product,
                        variants = variants,
                        isLoading = false,
                        selectedAttributes = emptyMap(),
                        selectedVariant = null
                    ) 
                }
            }.onFailure { error ->
                _productDetailsUiState.update { 
                    it.copy(
                        errorMessage = error.message ?: "Failed to load product details",
                        isLoading = false
                    ) 
                }
            }
        }
    }

    /**
     * Called when the user selects a specific attribute value.
     */
    fun onAttributeSelected(attributeName: String, value: String) {
        _productDetailsUiState.update { state ->
            val newSelectedAttributes = state.selectedAttributes.toMutableMap().apply {
                put(attributeName, value)
            }
            
            // Try to find a variant that matches the current selection combination
            val matchedVariant = state.variants.find { variant ->
                variant.combination == newSelectedAttributes
            }

            state.copy(
                selectedAttributes = newSelectedAttributes,
                selectedVariant = matchedVariant
            )
        }
    }
}
