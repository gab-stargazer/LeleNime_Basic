package com.lelestacia.lelenimexml.core.data.di

import android.content.Context
import com.lelestacia.lelenimexml.core.data.AnimeRepository
import com.lelestacia.lelenimexml.core.data.CharacterRepository
import com.lelestacia.lelenimexml.core.data.IAnimeRepository
import com.lelestacia.lelenimexml.core.data.ICharacterRepository
import com.lelestacia.lelenimexml.core.database.ILocalDataSource
import com.lelestacia.lelenimexml.core.database.user_pref.UserPref
import com.lelestacia.lelenimexml.core.network.INetworkDataSource
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
        apiService: INetworkDataSource,
        localDataSource: ILocalDataSource,
        userPref: UserPref
    ): IAnimeRepository =
        AnimeRepository(
            apiService, localDataSource, userPref
        )

    @Provides
    @Singleton
    fun provideCharacterRepository(
        apiService: INetworkDataSource,
        localDataSource: ILocalDataSource
    ): ICharacterRepository =
        CharacterRepository(
            apiService,
            localDataSource
        )
}
