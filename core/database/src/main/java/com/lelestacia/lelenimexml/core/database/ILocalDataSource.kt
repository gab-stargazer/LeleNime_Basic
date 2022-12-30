package com.lelestacia.lelenimexml.core.database

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.model.database.AnimeEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterDetailEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    /*Start of Anime Section*/
    suspend fun insertOrUpdateAnime(anime: AnimeEntity)
    suspend fun updateAnime(anime: AnimeEntity)
    suspend fun getAnimeByAnimeId(animeID: Int): AnimeEntity?
    fun getAllAnimeHistory(): PagingSource<Int, AnimeEntity>
    fun getNewestAnimeDataByAnimeId(animeID: Int): Flow<AnimeEntity>

    fun getAllFavoriteAnime(): PagingSource<Int, AnimeEntity>
    /*End Of Anime Section*/

    /*Start of Character Section*/
    suspend fun insertOrUpdateCharacter(character: List<CharacterEntity>)
    suspend fun insertOrReplaceAdditionalInformation(additionalInformation: CharacterAdditionalInformationEntity)
    suspend fun getCharacterAdditionalInformationById(characterID: Int): CharacterAdditionalInformationEntity?
    suspend fun getCharacterFullProfile(characterID: Int): CharacterDetailEntity
    suspend fun getAllCharacterFromAnimeById(animeID: Int): List<CharacterEntity>
    /*End of Character Section*/
}