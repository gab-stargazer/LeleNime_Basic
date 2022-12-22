package com.lelestacia.lelenimexml.feature.anime.domain.usecase

import com.lelestacia.lelenimexml.core.repository.CharacterRepository
import com.lelestacia.lelenimexml.feature.anime.domain.model.CharacterDetail
import com.lelestacia.lelenimexml.feature.anime.domain.util.CharacterMapper.entityToCharacter
import com.lelestacia.lelenimexml.feature.anime.domain.util.CharacterMapper.fullProfileEntityToFullProfile
import com.lelestacia.lelenimexml.feature.anime.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterInteractor @Inject constructor(
    private val characterRepository: CharacterRepository
) : CharacterUseCase {
    override fun getAnimeCharacterById(id: Int): Flow<List<Character>> =
        characterRepository.getAnimeCharactersById(id).map { characters ->
            characters.map(::entityToCharacter)
        }

    override fun getCharacterInformationByCharacterId(characterId: Int): Flow<CharacterDetail> =
        characterRepository.getCharacterDetailById(characterId).map(::fullProfileEntityToFullProfile)
}