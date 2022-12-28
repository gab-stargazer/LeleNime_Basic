package com.lelestacia.lelenimexml.feature.anime.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.lelenimexml.core.domain.usecase.IAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.ICharacterUseCase
import com.lelestacia.lelenimexml.core.model.domain.anime.Anime
import com.lelestacia.lelenimexml.core.model.domain.character.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAnimeViewModel @Inject constructor(
    private val characterUseCase: ICharacterUseCase,
    private val animeUseCase: IAnimeUseCase
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