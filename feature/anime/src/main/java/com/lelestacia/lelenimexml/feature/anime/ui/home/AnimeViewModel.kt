package com.lelestacia.lelenimexml.feature.anime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.IAnimeUseCase
import com.lelestacia.lelenimexml.core.model.domain.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val animeUseCase: IAnimeUseCase
) : ViewModel() {

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> get() = _searchQuery

    val getAnimeData: LiveData<PagingData<Anime>> = _searchQuery
        .switchMap { query ->
            if (query.isEmpty()) animeUseCase
                .getAiringAnime()
                .cachedIn(viewModelScope)
                .asLiveData()
            else animeUseCase
                .getAnimeByTitle(query)
                .cachedIn(viewModelScope)
                .asLiveData()
        }

    fun insertNewSearchQuery(newSearchQuery: String) {
        _searchQuery.value = newSearchQuery
    }

    fun insertOrUpdateAnimeToHistory(anime: Anime) = viewModelScope.launch {
        animeUseCase.insertOrUpdateNewAnimeToHistory(anime)
    }
}
