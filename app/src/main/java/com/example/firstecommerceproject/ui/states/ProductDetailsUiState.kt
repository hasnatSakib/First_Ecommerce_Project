package com.example.firstecommerceproject.ui.states

import com.example.firstecommerceproject.domain.models.Product

/**
 * Data class representing the visual state of the Product Details screen.
 *
 * This state is consumed by the UI to determine what to show the user (loading indicator,
 * error message, or product information).
 *
 * @property product The [Product] data fetched from the repository. Null if not loaded yet.
 * @property isLoading True if a network request is currently fetching the product data.
 * @property errorMessage A human-readable error string if the fetch operation failed.
 */
data class ProductDetailsUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
