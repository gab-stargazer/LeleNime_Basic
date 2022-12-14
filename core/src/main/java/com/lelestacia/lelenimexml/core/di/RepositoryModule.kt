package com.lelestacia.lelenimexml.core.di

import com.lelestacia.lelenimexml.core.repository.AnimeRepository
import com.lelestacia.lelenimexml.core.repository.AnimeRepositoryImpl
import com.lelestacia.lelenimexml.core.source.local.AnimeDatabase
import com.lelestacia.lelenimexml.core.source.remote.JikanAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAnimeRepository(
        apiService: JikanAPI,
        animeDatabase: AnimeDatabase
    ): AnimeRepository =
        AnimeRepositoryImpl(apiService, animeDatabase)
}