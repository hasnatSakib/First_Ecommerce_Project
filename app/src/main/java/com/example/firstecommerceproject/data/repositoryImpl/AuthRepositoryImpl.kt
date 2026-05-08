package com.example.firstecommerceproject.data.repositoryImpl

import com.example.firstecommerceproject.data.remote.api.FirebaseAuthService
import com.example.firstecommerceproject.domain.repository.AuthRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthService: FirebaseAuthService
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuthService.currentUser

    override suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val user = firebaseAuthService.signIn(email, password)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Login failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val user = firebaseAuthService.signUp(email, password)
            if (user != null) {
                val userData = mapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "fullName" to "$firstName $lastName",
                    "email" to email,
                    "phone" to phone,
                    "createdAt" to Timestamp.now(),
                    "fcmToken" to "" 
                )
                firebaseAuthService.saveUserData(user.uid, userData)
                Result.success(user)
            } else {
                Result.failure(Exception("Signup failed: User is null"))
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("This email address is already in use by another account."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendEmailVerification(): Result<Unit> {
        return try {
            firebaseAuthService.sendEmailVerification()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Boolean> {
        return try {
            firebaseAuthService.signOut()
            Result.success(true)
        } catch (
            e: Exception
        ) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(): Result<Boolean> {
        return try {
            firebaseAuthService.deleteAccount()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuthService.currentUser != null
    }

    override suspend fun getName(): Result<String?> {
        return try {
            val document = firebaseAuthService.getName()
            val name = document?.getString("fullName")
            Result.success(name)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
