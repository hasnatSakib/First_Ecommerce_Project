package com.example.firstecommerceproject.domain.use_case.data

import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

/**
 * Use case for retrieving all products from the repository.
 */
class GetProductsUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    /**
     * Executes the use case to fetch all products.
     * @return A [Result] containing the list of products or an exception.
     */
    suspend operator fun invoke(): Result<List<Product>?> {
        return dataRepository.getProducts()
    }
}
