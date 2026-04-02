package com.example.firstecommerceproject.domain.use_case

import com.example.firstecommerceproject.domain.use_case.data.GetBannersUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetNameUseCase

data class DataUseCases(
    val getName: GetNameUseCase,
    val getBanners: GetBannersUseCase
)
