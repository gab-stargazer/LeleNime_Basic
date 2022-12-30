package com.lelestacia.lelenimexml.core.database

import androidx.paging.PagingSource
import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.dao.CharacterDao
import com.lelestacia.lelenimexml.core.model.database.AnimeEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterDetailEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val animeDao: AnimeDao,
    private val characterDao: CharacterDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ILocalDataSource {

    override suspend fun insertOrUpdateAnime(anime: AnimeEntity) {
        withContext(ioDispatcher) {
            animeDao.insertOrUpdateAnime(anime)
        }
    }

    override suspend fun updateAnime(anime: AnimeEntity) {
        withContext(ioDispatcher) {
            animeDao.updateAnime(anime)
        }
    }

    override suspend fun getAnimeByAnimeId(animeID: Int): AnimeEntity? {
        return withContext(ioDispatcher) {
            animeDao.getAnimeByAnimeId(animeID)
        }
    }

    override fun getAllAnimeHistory(): PagingSource<Int, AnimeEntity> {
        return animeDao.getAllAnimeHistory()
    }

    override fun getNewestAnimeDataByAnimeId(animeID: Int): Flow<AnimeEntity> {
        return animeDao.getNewestAnimeDataByAnimeId(animeID)
    }

    override fun getAllFavoriteAnime(): PagingSource<Int, AnimeEntity> {
        return animeDao.getAllFavoriteAnime()
    }

    override suspend fun insertOrUpdateCharacter(character: List<CharacterEntity>) {
        withContext(ioDispatcher) {
            characterDao.insertOrUpdateCharacter(character)
        }
    }

    override suspend fun insertOrReplaceAdditionalInformation(additionalInformation: CharacterAdditionalInformationEntity) {
        withContext(ioDispatcher) {
            characterDao.insertOrReplaceAdditionalInformation(additionalInformation)
        }
    }

    override suspend fun getCharacterAdditionalInformationById(characterID: Int): CharacterAdditionalInformationEntity? {
        return withContext(ioDispatcher) {
            characterDao.getCharacterAdditionalInformationById(characterID)
        }
    }

    override suspend fun getCharacterFullProfile(characterID: Int): CharacterDetailEntity {
        return withContext(ioDispatcher) {
            characterDao.getCharacterFullProfile(characterID)
        }
    }

    override suspend fun getAllCharacterFromAnimeById(animeID: Int): List<CharacterEntity> {
        return withContext(ioDispatcher) {
            characterDao.getAllCharacterFromAnimeById(animeID)
        }
    }
}