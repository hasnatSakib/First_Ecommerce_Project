package com.example.firstecommerceproject.domain.use_case.data

import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.models.ProductVariant
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

/**
 * Use case for retrieving a product and its associated variants in one call.
 */
class GetProductWithVariantsUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    suspend operator fun invoke(productId: String): Result<Pair<Product?, List<ProductVariant>>> {
        return dataRepository.getProductWithVariants(productId)
    }
}
