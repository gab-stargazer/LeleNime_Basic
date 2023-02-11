package com.lelestacia.lelenimexml.core.network.di

import com.lelestacia.lelenimexml.core.network.impl.anime.AnimeNetworkService
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import com.lelestacia.lelenimexml.core.network.impl.character.CharacterNetworkService
import com.lelestacia.lelenimexml.core.network.impl.character.ICharacterNetworkService
import com.lelestacia.lelenimexml.core.network.source.endpoint.AnimeAPI
import com.lelestacia.lelenimexml.core.network.source.endpoint.CharacterAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAnimeDataSource(animeAPI: AnimeAPI): IAnimeNetworkService =
        AnimeNetworkService(animeAPI)

    @Provides
    @Singleton
    fun provideCharacterDataSource(
        animeAPI: AnimeAPI,
        characterAPI: CharacterAPI
    ): ICharacterNetworkService =
        CharacterNetworkService(
            animeAPI = animeAPI,
            characterAPI = characterAPI
        )
}
