package com.lelestacia.lelenimexml.core.di

import com.lelestacia.lelenimexml.core.source.remote.JikanAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJikanApi(): JikanAPI = Retrofit.Builder()
        .baseUrl(JikanAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(JikanAPI::class.java)
}