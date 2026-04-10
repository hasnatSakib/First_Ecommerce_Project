package com.example.firstecommerceproject.domain.use_case.data

import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

/**
 * Use case for retrieving products belonging to a specific category.
 *
 * @property dataRepository The repository to fetch product data from.
 */
class GetProductsByCategoryUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    /**
     * Executes the use case to fetch products for the given [category].
     *
     * @param category The name or ID of the category to filter products by.
     * @return A [Result] containing the list of products or an exception.
     */
    suspend operator fun invoke(category: String): Result<List<Product>?> {
        return dataRepository.getProductsByCategory(category)
    }
}
