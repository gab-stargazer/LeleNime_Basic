package com.lelestacia.lelenimexml.core.database.di

import com.lelestacia.lelenimexml.core.database.dao.*
import com.lelestacia.lelenimexml.core.database.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideCharacterDatabaseService(
        animeCharacterCrossRefDao: AnimeCharacterCrossRefDao,
        characterVoiceActorCrossRefDao: CharacterVoiceActorCrossRefDao,
        characterDao: CharacterDao,
        voiceActorDao: VoiceActorDao
    ): ICharacterDatabaseService =
        CharacterDatabaseService(
            animeCharactersCrossRefDao = animeCharacterCrossRefDao,
            characterVoiceActorsCrossRefDao = characterVoiceActorCrossRefDao,
            characterDao = characterDao,
            voiceActorDao = voiceActorDao
        )

    @Singleton
    @Provides
    fun provideEpisodeDatabaseService(
        episodeDao: EpisodeDao
    ): IEpisodeDatabaseService =
        EpisodeDatabaseService(
            episodeDao = episodeDao
        )

    @Singleton
    @Provides
    fun provideAnimeDatabaseService(animeDao: AnimeDao): IAnimeDatabaseService =
        AnimeDatabaseService(
            animeDao = animeDao
        )
}
