package com.example.firstecommerceproject.domain.use_case.data

import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

class GetNameUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(): Result<String?> {
        return repository.getName()
    }
}