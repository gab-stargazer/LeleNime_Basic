package com.lelestacia.lelenimexml.feature_anime.ui.detail_anime

import androidx.lifecycle.ViewModel
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailAnimeViewModel @Inject constructor(
    private val animeUseCases: AnimeUseCases
) : ViewModel() {
    fun getAnimeCharactersById(animeId: Int) = animeUseCases.getAnimeCharacterById(animeId)
}