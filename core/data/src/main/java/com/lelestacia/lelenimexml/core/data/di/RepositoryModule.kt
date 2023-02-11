package com.lelestacia.lelenimexml.core.data.di

import android.content.Context
import com.lelestacia.lelenimexml.core.common.Constant.USER_PREF
import com.lelestacia.lelenimexml.core.data.impl.anime.AnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.character.CharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.character.ICharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.episode.EpisodeRepository
import com.lelestacia.lelenimexml.core.data.impl.episode.IEpisodeRepository
import com.lelestacia.lelenimexml.core.data.impl.manga.IMangaRepository
import com.lelestacia.lelenimexml.core.data.impl.manga.MangaRepository
import com.lelestacia.lelenimexml.core.data.impl.recommendation.IRecommendationRepository
import com.lelestacia.lelenimexml.core.data.impl.recommendation.RecommendationRepository
import com.lelestacia.lelenimexml.core.data.utility.JikanErrorParserUtil
import com.lelestacia.lelenimexml.core.database.service.IAnimeDatabaseService
import com.lelestacia.lelenimexml.core.database.service.ICharacterDatabaseService
import com.lelestacia.lelenimexml.core.database.service.IEpisodeDatabaseService
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import com.lelestacia.lelenimexml.core.network.impl.character.ICharacterNetworkService
import com.lelestacia.lelenimexml.core.network.impl.manga.IMangaNetworkService
import com.lelestacia.lelenimexml.core.network.impl.recommendation.IRecommendationNetworkService
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
        apiService: IAnimeNetworkService,
        @ApplicationContext mContext: Context,
        animeDatabaseService: IAnimeDatabaseService
    ): IAnimeRepository =
        AnimeRepository(
            animeApiService = apiService,
            animeDatabaseService = animeDatabaseService,
            userPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE),
            errorParser = JikanErrorParserUtil()
        )

    @Provides
    @Singleton
    fun provideCharacterRepository(
        apiService: ICharacterNetworkService,
        characterDatabaseService: ICharacterDatabaseService
    ): ICharacterRepository =
        CharacterRepository(
            apiService = apiService,
            characterDatabaseService = characterDatabaseService,
            errorParser = JikanErrorParserUtil()
        )

    @Provides
    @Singleton
    fun provideEpisodeRepository(
        apiService: IAnimeNetworkService,
        episodeDatabaseService: IEpisodeDatabaseService,
        animeDatabaseService: IAnimeDatabaseService
    ): IEpisodeRepository = EpisodeRepository(
        animeApiService = apiService,
        episodeDatabaseService = episodeDatabaseService,
        animeDatabaseService = animeDatabaseService,
        errorParserUtil = JikanErrorParserUtil()
    )

    @Provides
    @Singleton
    fun provideMangaRepository(
        mangaAPI: IMangaNetworkService,
        @ApplicationContext mContext: Context
    ): IMangaRepository =
        MangaRepository(
            mangaAPI = mangaAPI,
            userPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        )

    @Provides
    @Singleton
    fun provideRecommendationRepository(
        recommendationAPI: IRecommendationNetworkService
    ): IRecommendationRepository =
        RecommendationRepository(
            recommendationAPI = recommendationAPI
        )
}
