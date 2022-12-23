package com.lelestacia.lelenimexml.feature.anime.ui.character_bottom_sheet

import androidx.lifecycle.ViewModel
import com.lelestacia.lelenimexml.feature.anime.domain.usecase.CharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterBottomSheetViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase
) : ViewModel() {

    fun getCharacterDetailByCharacterId(characterId: Int) =
        characterUseCase.getCharacterInformationByCharacterId(characterId)
}