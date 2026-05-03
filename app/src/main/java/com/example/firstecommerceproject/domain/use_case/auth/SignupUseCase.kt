package com.example.firstecommerceproject.domain.use_case.auth

import com.example.firstecommerceproject.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}\$".toRegex()
    
    // Password rules: 8+ chars, at least one digit, one uppercase, one lowercase, one special char
    private val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{8,}\$".toRegex()

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Result<FirebaseUser> {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return Result.failure(Exception("All fields are required"))
        }

        if (!email.matches(emailRegex)) {
            return Result.failure(Exception("Please enter a valid email address"))
        }

        if (!password.matches(passwordRegex)) {
            return Result.failure(Exception("Password must be at least 8 characters, contain a digit, uppercase, lowercase, and a special character"))
        }

        if (password != confirmPassword) {
            return Result.failure(Exception("Passwords do not match"))
        }
        
        val signupResult = repository.signup(firstName, lastName, email, phone, password)
        
        if (signupResult.isSuccess) {
            // Automatically send verification email on successful signup
            repository.sendEmailVerification()
        }
        
        return signupResult
    }
}
