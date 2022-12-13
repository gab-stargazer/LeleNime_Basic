package com.lelestacia.lelenimexml.feature_anime.ui.explore_anime

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreAnimeViewModel @Inject constructor(
    private val animeUseCases: AnimeUseCases
) : ViewModel() {

    private val searchQuery = MutableLiveData("")

    val searchAnimeByTitle = searchQuery
        .distinctUntilChanged()
        .switchMap {
        animeUseCases.searchAnimeByTitle(it)
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    fun searchAnime(newQuery: String) {
        searchQuery.value = newQuery
    }
}