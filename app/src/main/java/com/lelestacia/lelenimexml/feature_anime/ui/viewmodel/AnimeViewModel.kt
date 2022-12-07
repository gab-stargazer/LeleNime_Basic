package com.lelestacia.lelenimexml.feature_anime.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.feature_anime.domain.model.AnimeCard
import com.lelestacia.lelenimexml.feature_anime.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    fun seasonAnimePagingData(): Flow<PagingData<AnimeCard>> = repository.seasonAnimePagingData()
        .cachedIn(viewModelScope)

    fun searchAnimeByTitle(query: String) = repository.searchAnimeByTitle(query)
        .cachedIn(viewModelScope)
}