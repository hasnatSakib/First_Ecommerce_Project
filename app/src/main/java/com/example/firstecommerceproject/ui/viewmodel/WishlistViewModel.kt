package com.example.firstecommerceproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.DataUseCases
import com.example.firstecommerceproject.ui.states.WishlistUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Wishlist (Favourites) screen.
 */
@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val dataUseCases: DataUseCases
) : ViewModel() {

    private val _wishlistUiState = MutableStateFlow(WishlistUiState())
    val wishlistUiState = _wishlistUiState.asStateFlow()

    init {
        getWishlist()
    }

    /**
     * Fetches all products in the user's wishlist.
     */
    fun getWishlist() {
        _wishlistUiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = dataUseCases.getWishlist()
            _wishlistUiState.update { state ->
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
     * Removes a product from the wishlist.
     */
    fun toggleWishlist(productId: String) {
        viewModelScope.launch {
            val result = dataUseCases.toggleWishlist(productId)
            if (result.isSuccess) {
                // Refresh list after toggle
                getWishlist()
            }
        }
    }
}
