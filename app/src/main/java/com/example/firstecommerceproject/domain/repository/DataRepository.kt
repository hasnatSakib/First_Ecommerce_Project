package com.example.firstecommerceproject.domain.repository

interface DataRepository {
    suspend fun getName(): Result<String?>
    suspend fun getBanners(): Result<List<String>?>
}
