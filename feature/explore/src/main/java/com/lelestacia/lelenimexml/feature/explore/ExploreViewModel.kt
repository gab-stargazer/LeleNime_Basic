package com.lelestacia.lelenimexml.feature.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.explore.IExploreUseCases
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val explorePageUseCases: IExploreUseCases
) : ViewModel() {

    private val _searchQuery = MutableLiveData("")

    val topAnime: Flow<PagingData<Anime>> =
        explorePageUseCases
            .getTopAnime()
            .cachedIn(viewModelScope)

    val airingAnime: Flow<PagingData<Anime>> =
        explorePageUseCases
            .getAiringAnime()
            .cachedIn(viewModelScope)

    val upcomingAnime: Flow<PagingData<Anime>> =
        explorePageUseCases
            .getUpcomingAnime()
            .cachedIn(viewModelScope)

    val recommendationAnime: Flow<PagingData<Recommendation>> =
        explorePageUseCases
            .getRecentAnimeRecommendation()
            .cachedIn(viewModelScope)

    val topManga: Flow<PagingData<Manga>> =
        explorePageUseCases
            .getTopManga()
            .cachedIn(viewModelScope)

    fun insertNewSearchQuery(newSearchQuery: String) {
        _searchQuery.value = newSearchQuery
    }

    fun insertOrUpdateAnimeToHistory(anime: Anime) = viewModelScope.launch {
        explorePageUseCases.insertOrReplaceAnimeOnHistory(anime)
    }
}