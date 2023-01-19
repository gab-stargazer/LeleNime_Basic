package com.lelestacia.lelenimexml.feature.anime.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.IAnimeUseCase
import com.lelestacia.lelenimexml.core.model.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val animeUseCase: IAnimeUseCase
) : ViewModel() {

    val recentlyViewedAnime = animeUseCase
        .getAnimeHistory()
        .cachedIn(viewModelScope)

    fun insertOrUpdateAnime(anime: Anime) = viewModelScope.launch {
        animeUseCase.insertOrUpdateNewAnimeToHistory(anime)
    }
}
