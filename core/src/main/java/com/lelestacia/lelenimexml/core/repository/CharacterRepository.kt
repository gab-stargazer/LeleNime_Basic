package com.lelestacia.lelenimexml.core.repository

import com.lelestacia.lelenimexml.core.model.local.character.CharacterEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterDetailEntity
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAnimeCharactersById(animeId: Int) : Flow<List<CharacterEntity>>
    fun getCharacterDetailById(characterId: Int) : Flow<CharacterDetailEntity>
}