package com.example.firstecommerceproject.data.repositoryImpl

import com.example.firstecommerceproject.data.remote.api.FirebaseDataService
import com.example.firstecommerceproject.domain.models.Category
import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

/**
 * Implementation of [DataRepository] that uses Firebase as the data source.
 *
 * This class coordinates with [FirebaseDataService] to fetch data and maps 
 * raw Firebase responses (snapshots/documents) into domain-specific models.
 *
 * @property firebaseDataService The service responsible for low-level Firebase operations.
 */
class DataRepositoryImpl @Inject constructor(
    private val firebaseDataService: FirebaseDataService
) : DataRepository {

    /**
     * Fetches banner URLs from Firestore.
     */
    override suspend fun getBanners(): Result<List<String>?> {
        return try {
            val document = firebaseDataService.getBanners()

            @Suppress("UNCHECKED_CAST")
            val banners = document?.get("urls") as? List<String>
            Result.success(banners)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches and maps category documents into [Category] models.
     */
    override suspend fun getCategories(): Result<List<Category>?> {
        return try {
            val snapshot = firebaseDataService.getCategories()
            val categories = snapshot?.documents?.mapNotNull { doc ->
                val id = doc.id
                val name = doc.getString("name") ?: ""
                val imageUrl = doc.getString("imageUrl") ?: ""
                Category(id, name, imageUrl)
            }
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches all products and deserializes them into [Product] objects.
     */
    override suspend fun getProducts(): Result<List<Product>?> {
        return try {
            val snapshot = firebaseDataService.getProducts()
            val resultList = snapshot?.documents?.mapNotNull { product ->
                product.toObject(Product::class.java)
            }
            if (resultList?.isNotEmpty() ?: false) {
                Result.success(resultList)
            } else {
                Result.failure(Exception("No products found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches products by category and handles empty result scenarios.
     */
    override suspend fun getProductsByCategory(categoryId: String): Result<List<Product>?> {
        return try {
            val snapshot = firebaseDataService.getProductsByCategory(categoryId)
            val resultList = snapshot?.documents?.mapNotNull { product ->
                product.toObject(Product::class.java)
            }
            if (resultList?.isNotEmpty() ?: false) {
                Result.success(resultList)
            } else {
                Result.failure(Exception("No products found for this category"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches a specific product by ID and ensures the object is not null.
     */
    override suspend fun getProductById(productId: String): Result<Product?> {
        return try {
            val document = firebaseDataService.getProductById(productId)
            val product = document?.toObject(Product::class.java)
            if (product != null) {
                Result.success(product)
            } else {
                Result.failure(Exception("Product not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
