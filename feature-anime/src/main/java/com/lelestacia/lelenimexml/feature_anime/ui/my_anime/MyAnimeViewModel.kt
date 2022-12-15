package com.lelestacia.lelenimexml.feature_anime.ui.my_anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAnimeViewModel @Inject constructor(
    private val animeUseCases: AnimeUseCases
) : ViewModel() {

    val listOfAnime = animeUseCases.getAnimeHistory()
        .cachedIn(viewModelScope)
        .asLiveData()

    fun insertOrUpdateNewAnimeToHistory(animeEntity: AnimeEntity) {
        viewModelScope.launch {
            animeUseCases.insertOrUpdateNewAnimeToHistory(animeEntity)
        }
    }
}