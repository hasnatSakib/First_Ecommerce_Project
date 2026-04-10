package com.example.firstecommerceproject.domain.use_case.data

import com.example.firstecommerceproject.domain.models.Category
import com.example.firstecommerceproject.domain.repository.DataRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    suspend operator fun invoke(): Result<List<Category>?> {
        return dataRepository.getCategories()
    }
}
