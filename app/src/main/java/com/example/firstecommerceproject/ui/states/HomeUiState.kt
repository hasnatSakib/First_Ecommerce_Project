package com.example.firstecommerceproject.ui.states

import com.example.firstecommerceproject.domain.models.Category

/**
 * Represents the UI state for the Home screen.
 *
 * This data class encapsulates all the information needed to render the Home dashboard,
 * including user-specific data, promotional content, and loading/error states.
 *
 * @property name The display name of the logged-in user, if available.
 * @property banners A list of URLs for the promotional banners displayed at the top.
 * @property categories A list of product categories fetched from the backend.
 * @property isLoading Indicates if a data fetch operation is currently in progress.
 * @property errorMessage Contains a descriptive error message if a data fetch fails.
 */
data class HomeUiState(
    val name: String? = null,
    val banners: List<String> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
