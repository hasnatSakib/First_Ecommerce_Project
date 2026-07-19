package com.example.firstecommerceproject.domain.use_case.data

import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(productId: String) = repository.toggleFavorite(productId)
}

class GetFavoritesUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke() = repository.getFavoriteProducts()
}

class IsProductInFavoriteUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(productId: String) = repository.isProductInFavorite(productId)
}
