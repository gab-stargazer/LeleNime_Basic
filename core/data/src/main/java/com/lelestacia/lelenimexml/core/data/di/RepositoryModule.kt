package com.lelestacia.lelenimexml.core.data.di

import android.content.Context
import com.lelestacia.lelenimexml.core.common.Constant.USER_PREF
import com.lelestacia.lelenimexml.core.data.impl.anime.AnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.character.CharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.character.ICharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.episode.EpisodeRepository
import com.lelestacia.lelenimexml.core.data.impl.episode.IEpisodeRepository
import com.lelestacia.lelenimexml.core.data.utility.JikanErrorParserUtil
import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.dao.EpisodeDao
import com.lelestacia.lelenimexml.core.database.service.ICharacterDatabaseService
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
        @ApplicationContext mContext: Context,
        animeDao: AnimeDao,

    ): IAnimeRepository =
        AnimeRepository(
            apiService = apiService,
            animeDao = animeDao,
            userPreferences = mContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        )

    @Provides
    @Singleton
    fun provideCharacterRepository(
        apiService: ICharacterNetworkService,
        characterDatabaseService: ICharacterDatabaseService,
        errorParserUtil: JikanErrorParserUtil
    ): ICharacterRepository =
        CharacterRepository(
            apiService = apiService,
            characterDatabaseService = characterDatabaseService,
            errorParser = errorParserUtil
        )

    @Provides
    @Singleton
    fun provideEpisodeRepository(
        apiService: IAnimeNetworkService,
        errorParserUtil: JikanErrorParserUtil,
        episodeDao: EpisodeDao,
        animeDao: AnimeDao
    ): IEpisodeRepository = EpisodeRepository(
        apiService = apiService,
        errorParserUtil = errorParserUtil,
        episodeDao = episodeDao,
        animeDao = animeDao
    )
}
