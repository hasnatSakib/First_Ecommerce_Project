package com.example.firstecommerceproject.ui.states

import com.example.firstecommerceproject.domain.models.Product

/**
 * Represents the UI state for the Products By Category screen.
 *
 * @property products List of products in the selected category.
 * @property isLoading Indicates if a data fetch operation is in progress.
 * @property errorMessage Contains an error message if the fetch fails.
 */
data class ProductsByCategoryUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
