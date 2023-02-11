package com.lelestacia.lelenimexml.core.network.di

import com.lelestacia.lelenimexml.core.network.impl.anime.AnimeNetworkService
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import com.lelestacia.lelenimexml.core.network.impl.character.CharacterNetworkService
import com.lelestacia.lelenimexml.core.network.impl.character.ICharacterNetworkService
import com.lelestacia.lelenimexml.core.network.impl.manga.IMangaNetworkService
import com.lelestacia.lelenimexml.core.network.impl.manga.MangaNetworkService
import com.lelestacia.lelenimexml.core.network.impl.recommendation.IRecommendationNetworkService
import com.lelestacia.lelenimexml.core.network.impl.recommendation.RecommendationNetworkService
import com.lelestacia.lelenimexml.core.network.source.AnimeAPI
import com.lelestacia.lelenimexml.core.network.source.CharacterAPI
import com.lelestacia.lelenimexml.core.network.source.MangaAPI
import com.lelestacia.lelenimexml.core.network.source.RecommendationAPI
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

    @Provides
    @Singleton
    fun provideMangaDataSource(
        mangaAPI: MangaAPI
    ): IMangaNetworkService =
        MangaNetworkService(mangaAPI = mangaAPI)

    @Provides
    @Singleton
    fun provideRecommendationDataSource(
        recommendationAPI: RecommendationAPI
    ): IRecommendationNetworkService =
        RecommendationNetworkService(
            recommendationAPI = recommendationAPI
        )
}
