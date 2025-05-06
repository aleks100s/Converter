package com.alextos.app.navigation

import androidx.lifecycle.ViewModel
import com.alextos.converter.domain.storage.StorageService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ApplicationViewModel(
    storageService: StorageService
): ViewModel() {
    private val _isAdVisible = MutableStateFlow(false)
    val isAdVisible = _isAdVisible.asStateFlow()

    init {
        val isOnboardingFinished = storageService.isOnboardingFinished()
        val isFirstLaunch = storageService.isFirstLaunch()
        _isAdVisible.update {
            isOnboardingFinished && !isFirstLaunch
        }
    }
}