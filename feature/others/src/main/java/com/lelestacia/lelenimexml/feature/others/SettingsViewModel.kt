package com.lelestacia.lelenimexml.feature.others

import androidx.lifecycle.ViewModel
import com.lelestacia.lelenimexml.core.domain.usecase.anime.AnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val animeUseCase: AnimeUseCase
) : ViewModel() {
    fun isSafeMode() = animeUseCase.isSafeMode()
    fun changeSafeMode(isNsfwMode: Boolean) {
        animeUseCase.changeSafeMode(isNsfwMode)
    }
}
