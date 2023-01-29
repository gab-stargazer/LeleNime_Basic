package com.lelestacia.lelenimexml.core.data.di

import android.content.Context
import com.lelestacia.lelenimexml.core.data.impl.anime.AnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.character.CharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.character.ICharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.episode.EpisodeRepository
import com.lelestacia.lelenimexml.core.data.impl.episode.IEpisodeRepository
import com.lelestacia.lelenimexml.core.data.utility.JikanErrorParserUtil
import com.lelestacia.lelenimexml.core.database.impl.anime.IAnimeDatabaseService
import com.lelestacia.lelenimexml.core.database.impl.character.ICharacterDatabaseService
import com.lelestacia.lelenimexml.core.database.impl.episode.IEpisodeDatabaseService
import com.lelestacia.lelenimexml.core.network.impl.anime.IAnimeNetworkService
import com.lelestacia.lelenimexml.core.network.impl.character.ICharacterNetworkService
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
    fun provideJikanHttpErrorParser(): JikanErrorParserUtil = JikanErrorParserUtil()

    @Provides
    @Singleton
    fun provideAnimeRepository(
        apiService: IAnimeNetworkService,
        localDataSource: IAnimeDatabaseService,
        @ApplicationContext mContext: Context
    ): IAnimeRepository =
        AnimeRepository(
            apiService = apiService,
            localDataSource = localDataSource,
            mContext = mContext
        )

    @Provides
    @Singleton
    fun provideCharacterRepository(
        apiService: ICharacterNetworkService,
        localDataSource: ICharacterDatabaseService,
        errorParserUtil: JikanErrorParserUtil
    ): ICharacterRepository =
        CharacterRepository(
            apiService = apiService,
            localDataSource = localDataSource,
            errorParser = errorParserUtil
        )

    @Provides
    @Singleton
    fun provideEpisodeRepository(
        episodeDatabaseService: IEpisodeDatabaseService,
        animeDatabaseService: IAnimeDatabaseService,
        apiService: IAnimeNetworkService,
        errorParserUtil: JikanErrorParserUtil
    ): IEpisodeRepository = EpisodeRepository(
        episodeDatabaseService = episodeDatabaseService,
        animeDatabaseService = animeDatabaseService,
        apiService = apiService,
        errorParserUtil = errorParserUtil
    )
}
