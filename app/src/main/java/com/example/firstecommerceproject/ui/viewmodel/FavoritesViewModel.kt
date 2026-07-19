package com.example.firstecommerceproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.DataUseCases
import com.example.firstecommerceproject.ui.states.FavoritesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Favorites screen.
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val dataUseCases: DataUseCases
) : ViewModel() {

    private val _favoritesUiState = MutableStateFlow(FavoritesUiState())
    val favoritesUiState = _favoritesUiState.asStateFlow()

    init {
        getFavorites()
    }

    /**
     * Fetches all products in the user's favorites.
     */
    fun getFavorites() {
        _favoritesUiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = dataUseCases.getFavorites()
            _favoritesUiState.update { state ->
                result.fold(
                    onSuccess = { products ->
                        state.copy(isLoading = false, products = products)
                    },
                    onFailure = { error ->
                        state.copy(isLoading = false, errorMessage = error.message)
                    }
                )
            }
        }
    }

    /**
     * Toggles the favorite status of a product.
     */
    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            val result = dataUseCases.toggleFavorite(productId)
            if (result.isSuccess) {
                getFavorites()
            }
        }
    }
}
