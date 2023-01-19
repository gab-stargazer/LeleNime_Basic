package com.lelestacia.lelenimexml.core.domain.usecase

import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import kotlinx.coroutines.flow.Flow

interface ICharacterUseCase {
    fun getAnimeCharacterById(animeID: Int): Flow<List<Character>>
    fun getCharacterInformationByCharacterId(characterID: Int): Flow<CharacterDetail>
}
