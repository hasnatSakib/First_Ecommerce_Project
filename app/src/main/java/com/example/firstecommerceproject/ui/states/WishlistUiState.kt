package com.example.firstecommerceproject.ui.states

import com.example.firstecommerceproject.domain.models.Product

/**
 * UI State for the Wishlist screen.
 */
data class WishlistUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
