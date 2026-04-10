package com.example.firstecommerceproject.domain.repository

import com.example.firstecommerceproject.domain.models.Category
import com.example.firstecommerceproject.domain.models.Product

interface DataRepository {

    suspend fun getBanners(): Result<List<String>?>
    suspend fun getCategories(): Result<List<Category>?>
    suspend fun getProducts(): Result<List<Product>?>
    suspend fun getProductsByCategory(category: String): Result<List<Product>?>
}
