package com.lelestacia.lelenimexml.feature.anime.ui.bottom_sheet_character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.domain.usecase.character.ICharacterUseCase
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterBottomSheetViewModel @Inject constructor(
    private val characterUseCase: ICharacterUseCase
) : ViewModel() {

    private val _character: MutableStateFlow<Resource<CharacterDetail>> =
        MutableStateFlow(Resource.None)
    val character: StateFlow<Resource<CharacterDetail>> get() = _character.asStateFlow()

    fun getCharacterDetailByID(characterID: Int) = viewModelScope.launch {
        characterUseCase.getCharacterInformationByCharacterId(characterID)
            .collect { characterDetail ->
                _character.emit(characterDetail)
            }
    }
}
