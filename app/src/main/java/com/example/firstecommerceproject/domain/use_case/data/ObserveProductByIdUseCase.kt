package com.example.firstecommerceproject.domain.use_case.data

import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for observing a specific product by its ID in real-time.
 *
 * @property dataRepository The repository to observe product data from.
 */
class ObserveProductByIdUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    /**
     * Executes the use case to observe a product by its [productId].
     *
     * @param productId The unique identifier of the product.
     * @return A [Flow] of [Result]s containing the product updates.
     */
    operator fun invoke(productId: String): Flow<Result<Product?>> {
        return dataRepository.observeProductById(productId)
    }
}
