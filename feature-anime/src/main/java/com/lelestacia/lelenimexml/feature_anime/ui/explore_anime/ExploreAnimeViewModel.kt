package com.lelestacia.lelenimexml.feature_anime.ui.explore_anime

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeUseCases
import com.lelestacia.lelenimexml.feature_anime.domain.utility.AnimeMapperUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreAnimeViewModel @Inject constructor(
    private val animeUseCases: AnimeUseCases
) : ViewModel() {

    private val searchQuery = MutableLiveData("")

    val searchAnimeByTitle = searchQuery.switchMap {
        animeUseCases.searchAnimeByTitle(it).cachedIn(viewModelScope).asLiveData()
    }

    fun searchAnime(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun insertNewOrUpdateLastViewed(anime: Anime) {
        viewModelScope.launch {
            val localAnime = animeUseCases.getAnimeByAnimeId(anime.malId)
            val newData = AnimeMapperUtil.animeToEntity(
                anime = anime,
                isFavorite = localAnime?.isFavorite ?: false
            )
            animeUseCases.insertOrUpdateNewAnimeToHistory(newData)
        }
    }
}