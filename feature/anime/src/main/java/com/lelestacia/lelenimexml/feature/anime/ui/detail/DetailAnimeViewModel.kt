package com.lelestacia.lelenimexml.feature.anime.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.domain.usecase.IAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.ICharacterUseCase
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.model.character.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAnimeViewModel @Inject constructor(
    private val characterUseCase: ICharacterUseCase,
    private val animeUseCase: IAnimeUseCase
) : ViewModel() {

    private val _characters: MutableStateFlow<Resource<List<Character>>> =
        MutableStateFlow(Resource.None)
    val characters get() = _characters.asStateFlow()

    fun getAnimeCharactersById(animeId: Int) = viewModelScope.launch {
        characterUseCase.getAnimeCharacterById(animeId).collect { result ->
            _characters.emit(result)
        }
    }

    fun getAnimeByMalId(animeId: Int): Flow<Anime> =
        animeUseCase.getAnimeByMalID(animeId)

    fun updateAnimeFavorite(malID: Int) = viewModelScope.launch {
        animeUseCase.updateAnimeFavorite(malID)
    }
}
