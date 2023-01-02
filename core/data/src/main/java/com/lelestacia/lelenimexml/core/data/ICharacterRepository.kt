package com.lelestacia.lelenimexml.core.data

import com.lelestacia.lelenimexml.core.model.database.character.CharacterDetailEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface ICharacterRepository {
    fun getAnimeCharactersById(animeID: Int): Flow<List<CharacterEntity>>
    fun getCharacterDetailById(characterID: Int): Flow<CharacterDetailEntity>
}
