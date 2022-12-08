package com.lelestacia.lelenimexml.feature_anime.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.dto.animefull.AnimeFUll
import com.lelestacia.lelenimexml.core.utililty.NetworkResponse
import com.lelestacia.lelenimexml.feature_anime.domain.model.AnimeCard
import com.lelestacia.lelenimexml.feature_anime.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _anime = MutableLiveData<NetworkResponse<AnimeFUll>>()
    val anime get() = _anime as LiveData<NetworkResponse<AnimeFUll>>

    fun getAnimeById(animeID: Int) {
        viewModelScope.launch {
            _anime.value = repository.getAnimeById(animeID)
        }
    }

    fun seasonAnimePagingData(): Flow<PagingData<AnimeCard>> = repository.seasonAnimePagingData()
        .cachedIn(viewModelScope)

    fun searchAnimeByTitle() = repository.searchAnimeByTitle()
        .cachedIn(viewModelScope)

    fun searchNewQuery(newQuery: String) {
        repository.searchNewQuery(newQuery)
    }
}