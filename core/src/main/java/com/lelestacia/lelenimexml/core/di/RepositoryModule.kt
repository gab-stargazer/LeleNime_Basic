package com.lelestacia.lelenimexml.core.di

import android.content.Context
import com.lelestacia.lelenimexml.core.repository.AnimeRepository
import com.lelestacia.lelenimexml.core.repository.AnimeRepositoryImpl
import com.lelestacia.lelenimexml.core.repository.CharacterRepository
import com.lelestacia.lelenimexml.core.repository.CharacterRepositoryImpl
import com.lelestacia.lelenimexml.core.source.local.AnimeDatabase
import com.lelestacia.lelenimexml.core.source.remote.JikanAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAnimeRepository(
        apiService: JikanAPI,
        animeDatabase: AnimeDatabase,
        @ApplicationContext mContext: Context
    ): AnimeRepository =
        AnimeRepositoryImpl(apiService, animeDatabase, mContext)

    @Provides
    @Singleton
    fun provideCharacterRepository(
        apiService: JikanAPI,
        animeDatabase: AnimeDatabase
    ): CharacterRepository =
        CharacterRepositoryImpl(apiService, animeDatabase)
}