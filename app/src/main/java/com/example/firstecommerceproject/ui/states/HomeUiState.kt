package com.example.firstecommerceproject.ui.states

data class HomeUiState(
    val name: String? = null,
    val banners: List<String>? = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
