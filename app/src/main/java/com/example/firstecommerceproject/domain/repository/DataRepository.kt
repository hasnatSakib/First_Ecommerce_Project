package com.example.firstecommerceproject.domain.repository

import com.example.firstecommerceproject.domain.models.Category
import com.example.firstecommerceproject.domain.models.Product

/**
 * Repository interface for managing e-commerce data.
 *
 * Defines the contract for fetching banners, categories, and product information.
 * Implementing classes are responsible for determining the data source (Remote, Local, etc.).
 */
interface DataRepository {

    /**
     * Retrieves a list of promotional banner image URLs.
     * @return A [Result] containing a list of URLs or an error.
     */
    suspend fun getBanners(): Result<List<String>?>

    /**
     * Retrieves all available product categories.
     * @return A [Result] containing a list of [Category] objects or an error.
     */
    suspend fun getCategories(): Result<List<Category>?>

    /**
     * Retrieves all available products across all categories.
     * @return A [Result] containing a list of [Product] objects or an error.
     */
    suspend fun getProducts(): Result<List<Product>?>

    /**
     * Retrieves products filtered by a specific category identifier.
     * @param category The category name or ID to filter by.
     * @return A [Result] containing the filtered list of [Product]s or an error.
     */
    suspend fun getProductsByCategory(category: String): Result<List<Product>?>

    /**
     * Retrieves details for a specific product by its ID.
     * @param productId The unique identifier for the product.
     * @return A [Result] containing the [Product] details or an error.
     */
    suspend fun getProductById(productId: String): Result<Product?>
}
