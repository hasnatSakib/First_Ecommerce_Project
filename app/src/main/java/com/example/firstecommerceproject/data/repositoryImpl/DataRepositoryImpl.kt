package com.example.firstecommerceproject.data.repositoryImpl

import com.example.firstecommerceproject.data.remote.api.FirebaseDataService
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val firebaseDataService: FirebaseDataService
) : DataRepository {
    override suspend fun getName(): Result<String?> {
        return try {
            val document = firebaseDataService.getName()
            val name = document?.getString("name")
            Result.success(name)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
}
