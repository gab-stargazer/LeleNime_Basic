package com.lelestacia.lelenimexml.di

import com.lelestacia.lelenimexml.core.domain.remote.JikanAPI
import com.lelestacia.lelenimexml.feature_anime.domain.repository.AnimeRepository
import com.lelestacia.lelenimexml.feature_anime.domain.repository.AnimeRepositoryImpl
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
    fun provideAnimeRepository(animeApi: JikanAPI): AnimeRepository =
        AnimeRepositoryImpl(animeApi)
}