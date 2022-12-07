package com.lelestacia.lelenimexml.feature_anime.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.feature_anime.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    fun seasonAnimePagingData() = repository.seasonAnimePagingData()
        .cachedIn(viewModelScope)
}