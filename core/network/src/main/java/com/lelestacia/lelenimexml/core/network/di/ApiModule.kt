package com.lelestacia.lelenimexml.core.network.di

import com.lelestacia.lelenimexml.core.network.source.AnimeAPI
import com.lelestacia.lelenimexml.core.network.source.CharacterAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideSSL(): CertificatePinner = CertificatePinner.Builder()
        .add(HOSTNAME, "sha256/WxVeH3behrxKvQkDq0Rk1d7c8ZFEx/rxNV4XNhHszo8=")
        .add(HOSTNAME, "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=")
        .add(HOSTNAME, "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=").build()

    @Provides
    @Singleton
    fun provideDeserializer(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideAnimeService(
        okHttpClient: OkHttpClient,
        deserializer: GsonConverterFactory
    ): AnimeAPI =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(deserializer)
            .client(okHttpClient)
            .validateEagerly(true)
            .build()
            .create(AnimeAPI::class.java)

    @Provides
    @Singleton
    fun provideCharacterService(
        okHttpClient: OkHttpClient,
        deserializer: GsonConverterFactory
    ): CharacterAPI =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(deserializer)
            .client(okHttpClient)
            .validateEagerly(true)
            .build()
            .create(CharacterAPI::class.java)

    private const val HOSTNAME = "api.jikan.moe"
    private const val BASE_URL = "https://api.jikan.moe/v4/"
}
