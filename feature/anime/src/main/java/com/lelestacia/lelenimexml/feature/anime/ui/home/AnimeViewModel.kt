package com.lelestacia.lelenimexml.feature.anime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.anime.IAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.home.IHomeAnimeUseCase
import com.lelestacia.lelenimexml.core.model.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val commonAnimeUseCase: IAnimeUseCase,
    private val homeAnimeUseCase: IHomeAnimeUseCase
) : ViewModel() {

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> get() = _searchQuery

    val getAnimeData: LiveData<PagingData<Anime>> = _searchQuery
        .switchMap { query ->
            if (query.isEmpty()) homeAnimeUseCase
                .getAiringAnime()
                .cachedIn(viewModelScope)
                .asLiveData()
            else homeAnimeUseCase
                .getAnimeByTitle(query)
                .cachedIn(viewModelScope)
                .asLiveData()
        }

    fun insertNewSearchQuery(newSearchQuery: String) {
        _searchQuery.value = newSearchQuery
    }

    fun insertOrUpdateAnimeToHistory(anime: Anime) = viewModelScope.launch {
        commonAnimeUseCase.insertOrUpdateNewAnimeToHistory(anime)
    }
}
