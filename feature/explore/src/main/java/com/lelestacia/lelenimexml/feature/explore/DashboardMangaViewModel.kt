package com.lelestacia.lelenimexml.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.dashboard.manga.IDashboardMangaUseCases
import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DashboardMangaViewModel @Inject constructor(
    dashboardMangaUseCases: IDashboardMangaUseCases
) : ViewModel() {

    val topManga: Flow<PagingData<Manga>> = dashboardMangaUseCases
        .getTopManga()
        .cachedIn(viewModelScope)

    val mangaRecommendation : Flow<PagingData<Recommendation>> = dashboardMangaUseCases
        .getRecentMangaRecommendation()
        .cachedIn(viewModelScope)
}