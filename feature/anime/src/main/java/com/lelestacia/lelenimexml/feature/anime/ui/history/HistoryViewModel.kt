package com.lelestacia.lelenimexml.feature.anime.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.feature.anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature.anime.domain.usecase.AnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val animeUseCase: AnimeUseCase
)  : ViewModel() {

    val recentlyViewedAnime = animeUseCase
        .getAnimeHistory()
        .cachedIn(viewModelScope)

    fun insertOrUpdateAnime(anime: Anime) {
        viewModelScope.launch {
            animeUseCase.insertOrUpdateNewAnimeToHistory(anime)
        }
    }
}