package com.lelestacia.lelenimexml.core.database.di

import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.dao.CharacterDao
import com.lelestacia.lelenimexml.core.database.dao.EpisodeDao
import com.lelestacia.lelenimexml.core.database.impl.anime.AnimeDatabaseService
import com.lelestacia.lelenimexml.core.database.impl.anime.IAnimeDatabaseService
import com.lelestacia.lelenimexml.core.database.impl.character.CharacterDatabaseService
import com.lelestacia.lelenimexml.core.database.impl.character.ICharacterDatabaseService
import com.lelestacia.lelenimexml.core.database.impl.episode.EpisodeDatabaseService
import com.lelestacia.lelenimexml.core.database.impl.episode.IEpisodeDatabaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideAnimeDataSource(animeDao: AnimeDao): IAnimeDatabaseService =
        AnimeDatabaseService(
            animeDao = animeDao
        )

    @Provides
    @Singleton
    fun provideCharacterDataSource(characterDao: CharacterDao): ICharacterDatabaseService =
        CharacterDatabaseService(characterDao = characterDao)

    @Provides
    @Singleton
    fun provideEpisodeDataSource(episodeDao: EpisodeDao): IEpisodeDatabaseService =
        EpisodeDatabaseService(episodeDao = episodeDao)
}
