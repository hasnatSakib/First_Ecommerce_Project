package com.example.firstecommerceproject.domain.use_case

import com.example.firstecommerceproject.domain.use_case.data.GetBannersUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetCategoryUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetProductByIdUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetProductVariantsUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetProductWithVariantsUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetProductsByCategoryUseCase
import com.example.firstecommerceproject.domain.use_case.data.ObserveProductByIdUseCase

data class DataUseCases(
    val getBanners: GetBannersUseCase,
    val getCategories: GetCategoryUseCase,
    val getProductsByCategory: GetProductsByCategoryUseCase,
    val getProductById: GetProductByIdUseCase,
    val observeProductById: ObserveProductByIdUseCase,
    val getProductVariants: GetProductVariantsUseCase,
    val getProductWithVariants: GetProductWithVariantsUseCase
)
