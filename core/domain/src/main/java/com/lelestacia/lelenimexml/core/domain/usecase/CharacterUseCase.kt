package com.lelestacia.lelenimexml.core.domain.usecase

import com.lelestacia.lelenimexml.core.data.ICharacterRepository
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterUseCase @Inject constructor(
    private val characterRepository: ICharacterRepository
) : ICharacterUseCase {
    override fun getAnimeCharacterById(animeID: Int): Flow<List<Character>> =
        characterRepository.getAnimeCharactersById(animeID)

    override fun getCharacterInformationByCharacterId(characterID: Int): Flow<CharacterDetail> =
        characterRepository.getCharacterDetailById(characterID)
}
