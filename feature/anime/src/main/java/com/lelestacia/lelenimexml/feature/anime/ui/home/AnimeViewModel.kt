package com.lelestacia.lelenimexml.feature.anime.ui.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.IAnimeUseCase
import com.lelestacia.lelenimexml.core.model.domain.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val animeUseCase: IAnimeUseCase
) : ViewModel() {

    private val searchQuery = MutableLiveData("")

    val getAnimeData: LiveData<PagingData<Anime>> = searchQuery
        .distinctUntilChanged()
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
        searchQuery.value = newSearchQuery
    }

    suspend fun insertOrUpdateAnimeToHistory(anime: Anime) {
        animeUseCase.insertOrUpdateNewAnimeToHistory(anime)
    }
}