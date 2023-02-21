package com.lelestacia.lelenimexml.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.dashboard.anime.IDashboardAnimeUseCases
import com.lelestacia.lelenimexml.core.domain.usecase.dashboard.manga.IDashboardMangaUseCases
import com.lelestacia.lelenimexml.core.model.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExpandedViewModel @Inject constructor(
    private val dashboardAnimeUseCases: IDashboardAnimeUseCases,
    private val dashboardMangaUseCases: IDashboardMangaUseCases
) : ViewModel() {

    val trendingAnime = dashboardAnimeUseCases.getTopAnime().cachedIn(viewModelScope)
    val airingAnime = dashboardAnimeUseCases.getAiringAnime().cachedIn(viewModelScope)
    val upcomingAnime = dashboardAnimeUseCases.getUpcomingAnime().cachedIn(viewModelScope)
    val trendingManga = dashboardMangaUseCases.getTopManga().cachedIn(viewModelScope)

    fun insertOrUpdateAnimeToHistory(anime: Anime) = viewModelScope.launch {
        dashboardAnimeUseCases.insertOrReplaceAnimeOnHistory(anime)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.w("ViewModel Expanded Fragment was cleared")
    }
}