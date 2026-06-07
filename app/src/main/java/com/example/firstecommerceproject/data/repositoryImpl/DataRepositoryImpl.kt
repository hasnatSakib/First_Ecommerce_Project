package com.example.firstecommerceproject.data.repositoryImpl

import com.example.firstecommerceproject.data.remote.api.FirebaseDataService
import com.example.firstecommerceproject.domain.models.Category
import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.models.ProductVariant
import com.example.firstecommerceproject.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
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
            val banners = document?.get("imageUrls") as? List<String>
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
    override suspend fun getProductsByCategory(category: String): Result<List<Product>?> {
        return try {
            val snapshot = firebaseDataService.getProductsByCategory(category)
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
     * Fetches a specific product by ID and its variants if it has them.
     */
    override suspend fun getProductWithVariants(productId: String): Result<Pair<Product?, List<ProductVariant>>> {
        return try {
            val productSnapshot = firebaseDataService.getProductById(productId)
            val product = productSnapshot?.toObject(Product::class.java)
            
            val variants = if (product?.hasVariants == true) {
                val variantsSnapshot = firebaseDataService.getProductVariants(productId)
                variantsSnapshot?.documents?.mapNotNull { it.toObject(ProductVariant::class.java) } ?: emptyList()
            } else {
                emptyList()
            }
            
            Result.success(Pair(product, variants))
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
            val product = document?.toObject(Product::class.java)?.copy(id = document.id)
            if (product != null) {
                Result.success(product)
            } else {
                Result.failure(Exception("Product not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Observes a specific product by its ID in real-time.
     */
    override fun observeProductById(productId: String): Flow<Result<Product?>> {
        return firebaseDataService.observeProductById(productId)
            .map { document ->
                val product = document?.toObject(Product::class.java)?.copy(id = document.id)
                if (product != null) {
                    Result.success(product)
                } else {
                    Result.failure(Exception("Product not found"))
                }
            }
            .catch { e ->
                emit(Result.failure(e))
            }
    }

    /**
     * Retrieves all available variants for a specific product.
     */
    override suspend fun getProductVariants(productId: String): Result<List<ProductVariant>?> {
        return try {
            val snapshot = firebaseDataService.getProductVariants(productId)
            val variants = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(ProductVariant::class.java)
            }
            Result.success(variants)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleWishlist(productId: String): Result<Unit> {
        return try {
            firebaseDataService.toggleWishlist(productId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isProductInWishlist(productId: String): Result<Boolean> {
        return try {
            val result = firebaseDataService.isProductInWishlist(productId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWishlistProducts(): Result<List<Product>> {
        return try {
            val productIds = firebaseDataService.getWishlistProductIds()
            if (productIds.isEmpty()) return Result.success(emptyList())

            val products = productIds.mapNotNull { id ->
                firebaseDataService.getProductById(id)?.toObject(Product::class.java)?.copy(id = id)
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
