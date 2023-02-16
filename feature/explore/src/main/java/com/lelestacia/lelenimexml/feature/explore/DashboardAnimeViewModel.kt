package com.lelestacia.lelenimexml.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.explore.IExploreUseCases
import com.lelestacia.lelenimexml.core.model.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardAnimeViewModel @Inject constructor(
    private val animeUseCase: IExploreUseCases,
) : ViewModel() {

    val topAnime: Flow<PagingData<Anime>> = animeUseCase.getTopAnime()
        .cachedIn(viewModelScope)

    val airingAnime: Flow<PagingData<Anime>> = animeUseCase.getAiringAnime()
        .cachedIn(viewModelScope)

    val upcomingAnime: Flow<PagingData<Anime>> = animeUseCase.getUpcomingAnime()
        .cachedIn(viewModelScope)

    val animeRecommendation = animeUseCase.getRecentAnimeRecommendation()
        .cachedIn(viewModelScope)

    override fun onCleared() {
        super.onCleared()
        Timber.w("Explore ViewModel was cleared")
    }
}