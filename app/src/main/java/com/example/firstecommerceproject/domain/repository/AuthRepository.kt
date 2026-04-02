package com.example.firstecommerceproject.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Result<FirebaseUser>
    suspend fun logout(): Result<Boolean>
    fun isUserLoggedIn(): Boolean
}
