package com.example.firstecommerceproject.domain.use_case

import com.example.firstecommerceproject.domain.use_case.data.GetBannersUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetCategoryUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetProductByIdUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetProductsByCategoryUseCase

data class DataUseCases(
    val getBanners: GetBannersUseCase,
    val getCategories: GetCategoryUseCase,
    val getProductsByCategory: GetProductsByCategoryUseCase,
    val getProductById: GetProductByIdUseCase
)
