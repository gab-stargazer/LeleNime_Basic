package com.lelestacia.lelenimexml.feature.anime.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.lelenimexml.feature.anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature.anime.domain.model.Character
import com.lelestacia.lelenimexml.feature.anime.domain.usecase.AnimeUseCase
import com.lelestacia.lelenimexml.feature.anime.domain.usecase.CharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAnimeViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase,
    private val animeUseCase: AnimeUseCase
) : ViewModel() {
    fun getAnimeCharactersById(animeId: Int): Flow<List<Character>> =
        characterUseCase.getAnimeCharacterById(animeId)

    suspend fun getAnimeByMalId(animeId: Int): Flow<Anime> =
        animeUseCase.getAnimeByMalID(animeId)

    fun updateAnimeFavorite(malID: Int) {
        viewModelScope.launch {
            animeUseCase.updateAnimeFavorite(malID)
        }
    }
}