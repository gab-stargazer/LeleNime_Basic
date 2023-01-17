package com.lelestacia.lelenimexml.core.data

import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import kotlinx.coroutines.flow.Flow

interface ICharacterRepository {
    fun getAnimeCharactersById(animeID: Int): Flow<List<Character>>
    fun getCharacterDetailById(characterID: Int): Flow<CharacterDetail>
}
