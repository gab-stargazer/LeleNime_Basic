package com.lelestacia.lelenimexml.feature.anime.ui.bottom_sheet_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.lelenimexml.core.domain.usecase.IAnimeUseCase
import com.lelestacia.lelenimexml.core.model.domain.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuBottomSheetViewModel @Inject constructor(
    private val animeUseCase: IAnimeUseCase
) : ViewModel() {

    suspend fun getAnimeNewestData(malID: Int): Flow<Anime> =
        animeUseCase.getAnimeByMalID(malID)

    fun updateAnimeFavorite(malID: Int) {
        viewModelScope.launch {
            animeUseCase.updateAnimeFavorite(malID)
        }
    }

    suspend fun insertOrUpdateAnime(anime: Anime) {
        animeUseCase.insertOrUpdateNewAnimeToHistory(anime)
    }
}