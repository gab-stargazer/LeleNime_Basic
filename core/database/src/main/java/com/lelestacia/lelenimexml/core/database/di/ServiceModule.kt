package com.lelestacia.lelenimexml.core.database.di

import com.lelestacia.lelenimexml.core.database.dao.AnimeCharacterCrossRefDao
import com.lelestacia.lelenimexml.core.database.dao.CharacterDao
import com.lelestacia.lelenimexml.core.database.dao.CharacterVoiceActorCrossRefDao
import com.lelestacia.lelenimexml.core.database.dao.VoiceActorDao
import com.lelestacia.lelenimexml.core.database.service.CharacterDatabaseService
import com.lelestacia.lelenimexml.core.database.service.ICharacterDatabaseService
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
}
