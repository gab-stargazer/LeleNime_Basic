package com.lelestacia.lelenimexml.feature_anime.domain.usecases

import com.lelestacia.lelenimexml.feature_anime.domain.model.Character
import com.lelestacia.lelenimexml.feature_anime.domain.model.CharacterDetail
import kotlinx.coroutines.flow.Flow

interface CharacterUseCases {
    fun getAnimeCharacterById(id: Int): Flow<List<Character>>
    fun getCharacterInformationByCharacterId(characterId: Int): Flow<CharacterDetail>
}