package com.lelestacia.lelenimexml.core.data.di

import android.content.Context
import com.lelestacia.lelenimexml.core.data.impl.anime.AnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.character.CharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.character.ICharacterRepository
import com.lelestacia.lelenimexml.core.data.utility.JikanErrorParserUtil
import com.lelestacia.lelenimexml.core.database.IAnimeDatabaseService
import com.lelestacia.lelenimexml.core.database.ICharacterDatabaseService
import com.lelestacia.lelenimexml.core.network.INetworkAnimeService
import com.lelestacia.lelenimexml.core.network.INetworkCharacterService
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
        apiService: INetworkAnimeService,
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
    fun provideJikanHttpErrorParser(): JikanErrorParserUtil = JikanErrorParserUtil()

    @Provides
    @Singleton
    fun provideCharacterRepository(
        apiService: INetworkCharacterService,
        localDataSource: ICharacterDatabaseService,
        errorParserUtil: JikanErrorParserUtil
    ): ICharacterRepository =
        CharacterRepository(
            apiService = apiService,
            localDataSource = localDataSource,
            errorParser = errorParserUtil
        )
}
