package com.example.firstecommerceproject.domain.use_case.auth

import com.example.firstecommerceproject.domain.repository.AuthRepository
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

class GetNameUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<String?> {
        return repository.getName()
    }
}