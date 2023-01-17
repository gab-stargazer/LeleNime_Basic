package com.lelestacia.lelenimexml.feature.anime.ui.bottom_sheet_character

import androidx.lifecycle.ViewModel
import com.lelestacia.lelenimexml.core.domain.usecase.ICharacterUseCase
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharacterBottomSheetViewModel @Inject constructor(
    private val characterUseCase: ICharacterUseCase
) : ViewModel() {

    fun getCharacterDetailByCharacterId(characterId: Int): Flow<CharacterDetail> =
        characterUseCase.getCharacterInformationByCharacterId(characterId)
}
