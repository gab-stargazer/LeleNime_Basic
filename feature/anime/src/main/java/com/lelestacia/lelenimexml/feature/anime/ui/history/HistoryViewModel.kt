package com.lelestacia.lelenimexml.feature.anime.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.anime.IAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.history.IHistoryAnimeUseCase
import com.lelestacia.lelenimexml.core.model.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val commonAnimeUseCase: IAnimeUseCase,
    historyAnimeUseCase: IHistoryAnimeUseCase
) : ViewModel() {

    val recentlyViewedAnime: Flow<PagingData<Anime>> = historyAnimeUseCase
        .getAnimeHistory()
        .cachedIn(viewModelScope)

    fun insertOrUpdateAnime(anime: Anime) = viewModelScope.launch {
        commonAnimeUseCase.insertOrUpdateNewAnimeToHistory(anime)
    }
}
