package com.example.firstecommerceproject.domain.use_case

import com.example.firstecommerceproject.domain.use_case.data.*

data class DataUseCases(
    val getBanners: GetBannersUseCase,
    val getCategories: GetCategoryUseCase,
    val getProductsByCategory: GetProductsByCategoryUseCase,
    val getProductById: GetProductByIdUseCase,
    val observeProductById: ObserveProductByIdUseCase,
    val getProductVariants: GetProductVariantsUseCase,
    val getProductWithVariants: GetProductWithVariantsUseCase,
    val getProducts: GetProductsUseCase,
    val toggleWishlist: ToggleWishlistUseCase,
    val getWishlist: GetWishlistUseCase,
    val isProductInWishlist: IsProductInWishlistUseCase
)
