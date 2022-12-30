package com.lelestacia.lelenimexml.feature.anime.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.domain.usecase.IAnimeUseCase
import com.lelestacia.lelenimexml.core.model.domain.anime.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val animeUseCase: IAnimeUseCase
) : ViewModel() {

    val favoriteAnime : Flow<PagingData<Anime>> =
        animeUseCase
            .getAllFavoriteAnime()
            .cachedIn(viewModelScope)

    fun insertOrUpdateAnime(anime: Anime) {
        viewModelScope.launch {
            animeUseCase.insertOrUpdateNewAnimeToHistory(anime)
        }
    }
}