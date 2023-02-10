package com.lelestacia.lelenimexml.feature.explore

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.anime.IAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.history.IHistoryAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.home.IHomeAnimeUseCase
import com.lelestacia.lelenimexml.core.model.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val commonAnimeUseCase: IAnimeUseCase,
    homeAnimeUseCase: IHomeAnimeUseCase,
    historyAnimeUseCase: IHistoryAnimeUseCase
) : ViewModel() {

    private val _searchQuery = MutableLiveData("")

    val topAnime: Flow<PagingData<Anime>> = homeAnimeUseCase.getTopAnime()
        .cachedIn(viewModelScope)

    val airingAnime: Flow<PagingData<Anime>> = homeAnimeUseCase.getAiringAnime()
        .cachedIn(viewModelScope)

    val historyAnime: Flow<PagingData<Anime>> = historyAnimeUseCase.getAnimeHistory().cachedIn(viewModelScope)

    fun insertNewSearchQuery(newSearchQuery: String) {
        _searchQuery.value = newSearchQuery
    }

    fun insertOrUpdateAnimeToHistory(anime: Anime) = viewModelScope.launch {
        commonAnimeUseCase.insertOrUpdateNewAnimeToHistory(anime)
    }
}