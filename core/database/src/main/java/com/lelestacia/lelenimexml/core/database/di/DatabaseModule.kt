package com.lelestacia.lelenimexml.core.database.di

import android.content.Context
import androidx.room.Room
import com.lelestacia.lelenimexml.core.database.AnimeLocalDataSource
import com.lelestacia.lelenimexml.core.database.CharacterLocalDataSource
import com.lelestacia.lelenimexml.core.database.IAnimeLocalDataSource
import com.lelestacia.lelenimexml.core.database.ICharacterLocalDataSource
import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.dao.CharacterDao
import com.lelestacia.lelenimexml.core.database.database.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): AnimeDatabase =
        Room
            .databaseBuilder(
                context,
                AnimeDatabase::class.java,
                "anime.db"
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideAnimeDao(animeDatabase: AnimeDatabase): AnimeDao = animeDatabase.animeDao()

    @Provides
    @Singleton
    fun provideCharacterDao(animeDatabase: AnimeDatabase): CharacterDao =
        animeDatabase.characterDao()

    @Provides
    @Singleton
    fun provideAnimeDataSource(animeDao: AnimeDao): IAnimeLocalDataSource =
        AnimeLocalDataSource(animeDao)

    @Provides
    @Singleton
    fun provideCharacterDataSource(characterDao: CharacterDao): ICharacterLocalDataSource =
        CharacterLocalDataSource(characterDao)
}
