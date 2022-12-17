package com.lelestacia.lelenimexml.feature_anime.domain.usecases

import com.lelestacia.lelenimexml.core.repository.CharacterRepository
import com.lelestacia.lelenimexml.feature_anime.domain.model.Character
import com.lelestacia.lelenimexml.feature_anime.domain.model.CharacterDetail
import com.lelestacia.lelenimexml.feature_anime.domain.utility.CharacterMapper.entityToCharacter
import com.lelestacia.lelenimexml.feature_anime.domain.utility.CharacterMapper.fullProfileEntityToFullProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterInteractor @Inject constructor(
    private val characterRepository: CharacterRepository
) : CharacterUseCases {
    override fun getAnimeCharacterById(id: Int): Flow<List<Character>> =
        characterRepository.getAnimeCharactersById(id).map { characters ->
            characters.map(::entityToCharacter)
        }

    override fun getCharacterInformationByCharacterId(characterId: Int): Flow<CharacterDetail> =
        characterRepository.getCharacterDetailById(characterId).map(::fullProfileEntityToFullProfile)
}