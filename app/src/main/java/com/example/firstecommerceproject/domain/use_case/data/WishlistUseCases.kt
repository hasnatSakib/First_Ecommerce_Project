package com.example.firstecommerceproject.domain.use_case.data

import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

class ToggleWishlistUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(productId: String) = repository.toggleWishlist(productId)
}

class GetWishlistUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke() = repository.getWishlistProducts()
}

class IsProductInWishlistUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(productId: String) = repository.isProductInWishlist(productId)
}
