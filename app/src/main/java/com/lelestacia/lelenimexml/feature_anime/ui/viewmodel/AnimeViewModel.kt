package com.lelestacia.lelenimexml.feature_anime.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.dto.animefull.AnimeFUll
import com.lelestacia.lelenimexml.core.utililty.NetworkResponse
import com.lelestacia.lelenimexml.feature_anime.data.repository.AnimeRepository
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _anime = MutableLiveData<NetworkResponse<AnimeFUll>>()
    val anime get() = _anime as LiveData<NetworkResponse<AnimeFUll>>
    private val query = MutableLiveData("")

    fun searchAnimeByTitle(): LiveData<PagingData<Anime>> = query.distinctUntilChanged().switchMap {
        repository.searchAnimeByTitle(it)
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    fun searchAnime(newQuery: String) {
        query.value = newQuery
    }
}