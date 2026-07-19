package com.example.firstecommerceproject.ui.states

import com.example.firstecommerceproject.domain.models.Product

/**
 * Data class representing the visual state of the Favorites screen.
 */
data class FavoritesUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
