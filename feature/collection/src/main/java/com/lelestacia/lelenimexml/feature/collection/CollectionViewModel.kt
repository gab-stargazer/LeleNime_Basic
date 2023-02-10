package com.lelestacia.lelenimexml.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.history.IHistoryAnimeUseCase
import com.lelestacia.lelenimexml.core.model.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    animeUseCase: IHistoryAnimeUseCase
) : ViewModel() {

    val historyAnime: Flow<PagingData<Anime>> = animeUseCase
        .getAnimeHistory()
        .cachedIn(viewModelScope)
}