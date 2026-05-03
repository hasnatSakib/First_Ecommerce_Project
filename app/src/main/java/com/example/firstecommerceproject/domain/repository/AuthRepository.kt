package com.example.firstecommerceproject.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun signup(firstName: String, lastName: String, email: String, phone: String, password: String): Result<FirebaseUser>
    suspend fun sendEmailVerification(): Result<Unit>
    suspend fun logout(): Result<Boolean>
    fun isUserLoggedIn(): Boolean
    suspend fun getName(): Result<String?>
}
