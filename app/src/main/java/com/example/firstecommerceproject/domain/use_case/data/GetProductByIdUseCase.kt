package com.example.firstecommerceproject.domain.use_case.data

import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

/**
 * Use case for retrieving a specific product by its ID.
 *
 * @property dataRepository The repository to fetch product data from.
 */
class GetProductByIdUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    /**
     * Executes the use case to fetch a product by its [productId].
     *
     * @param productId The unique identifier of the product.
     * @return A [Result] containing the product or an exception.
     */
    suspend operator fun invoke(productId: String): Result<Product?> {
        return dataRepository.getProductById(productId)
    }
}
