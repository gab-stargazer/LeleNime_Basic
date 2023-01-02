package com.lelestacia.lelenimexml.core.domain.usecase

import com.lelestacia.lelenimexml.core.data.ICharacterRepository
import com.lelestacia.lelenimexml.core.model.domain.character.Character
import com.lelestacia.lelenimexml.core.model.domain.character.CharacterDetail
import com.lelestacia.lelenimexml.core.model.domain.character.asCharacter
import com.lelestacia.lelenimexml.core.model.domain.character.asCharacterDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterUseCase @Inject constructor(
    private val characterRepository: ICharacterRepository
) : ICharacterUseCase {
    override fun getAnimeCharacterById(animeID: Int): Flow<List<Character>> =
        characterRepository.getAnimeCharactersById(animeID).map { characters ->
            characters.map { it.asCharacter() }
        }

    override fun getCharacterInformationByCharacterId(characterID: Int): Flow<CharacterDetail> =
        characterRepository.getCharacterDetailById(characterID).map { it.asCharacterDetail() }
}
