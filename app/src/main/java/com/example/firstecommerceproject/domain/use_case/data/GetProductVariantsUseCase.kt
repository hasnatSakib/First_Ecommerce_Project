package com.example.firstecommerceproject.domain.use_case.data

import com.example.firstecommerceproject.domain.models.ProductVariant
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

/**
 * Use case for retrieving variants of a specific product.
 *
 * @property dataRepository The repository to fetch variant data from.
 */
class GetProductVariantsUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    /**
     * Executes the use case to fetch variants for the given [productId].
     *
     * @param productId The ID of the parent product.
     * @return A [Result] containing the list of variants or an exception.
     */
    suspend operator fun invoke(productId: String): Result<List<ProductVariant>?> {
        return dataRepository.getProductVariants(productId)
    }
}
