package com.lelestacia.lelenimexml.core.domain.usecase.character

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import kotlinx.coroutines.flow.Flow

interface ICharacterUseCase {
    fun getAnimeCharacterById(animeID: Int): Flow<Resource<List<Character>>>
    fun getCharacterInformationByCharacterId(characterID: Int): Flow<Resource<CharacterDetail>>
}
