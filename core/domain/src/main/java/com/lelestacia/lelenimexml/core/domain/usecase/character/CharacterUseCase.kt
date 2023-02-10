package com.lelestacia.lelenimexml.core.domain.usecase.character

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.impl.character.ICharacterRepository
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterUseCase @Inject constructor(
    private val characterRepository: ICharacterRepository
) : ICharacterUseCase {
    override fun getAnimeCharacterById(animeID: Int): Flow<Resource<List<Character>>> =
        characterRepository.getAnimeCharactersByAnimeID(animeID)

    override fun getCharacterInformationByCharacterId(characterID: Int): Flow<Resource<CharacterDetail>> =
        characterRepository.getCharacterDetailById(characterID)
}
