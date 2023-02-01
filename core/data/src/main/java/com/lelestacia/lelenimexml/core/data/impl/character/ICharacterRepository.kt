package com.lelestacia.lelenimexml.core.data.impl.character

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import kotlinx.coroutines.flow.Flow

interface ICharacterRepository {
    fun getAnimeCharactersByAnimeID(animeID: Int): Flow<Resource<List<Character>>>
    fun getCharacterDetailById(characterID: Int): Flow<Resource<CharacterDetail>>
}
