package com.example.firstecommerceproject.ui.states

import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.models.ProductVariant

/**
 * Data class representing the visual state of the Product Details screen.
 */
data class ProductDetailsUiState(
    val product: Product? = null,
    val variants: List<ProductVariant> = emptyList(),
    val selectedAttributes: Map<String, String> = emptyMap(),
    val selectedVariant: ProductVariant? = null,
    val isFavourite: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
