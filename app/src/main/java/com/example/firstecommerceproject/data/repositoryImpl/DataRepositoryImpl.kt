package com.example.firstecommerceproject.data.repositoryImpl

import com.example.firstecommerceproject.data.remote.api.FirebaseAuthService
import com.example.firstecommerceproject.data.remote.api.FirebaseDataService
import com.example.firstecommerceproject.domain.models.Category
import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val firebaseDataService: FirebaseDataService
) : DataRepository {


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
}
