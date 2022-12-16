package com.lelestacia.lelenimexml.feature_anime.ui.character_bottom_sheet

import androidx.lifecycle.ViewModel
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterBottomSheetViewModel @Inject constructor(
    private val animeUseCases: AnimeUseCases
) : ViewModel() {

    fun getCharacterDetailByCharacterId(characterId: Int) =
        animeUseCases.getCharacterInformationByCharacterId(characterId)
}