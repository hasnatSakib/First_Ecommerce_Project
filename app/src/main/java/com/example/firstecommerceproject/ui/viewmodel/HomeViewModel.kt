package com.example.firstecommerceproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstecommerceproject.domain.use_case.DataUseCases
import com.example.firstecommerceproject.ui.states.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataUseCases: DataUseCases
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    init {
        getHomeData()
    }

    fun getHomeData() {
        _homeUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val nameResult = dataUseCases.getName()
            val bannersResult = dataUseCases.getBanners()

            _homeUiState.update { state ->
                state.copy(
                    isLoading = false,
                    name = nameResult.getOrNull(),
                    banners = bannersResult.getOrNull() ?: emptyList(),
                    errorMessage = (nameResult.exceptionOrNull()
                        ?: bannersResult.exceptionOrNull())?.message
                )
            }
        }
    }
}
