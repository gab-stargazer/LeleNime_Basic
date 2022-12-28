package com.lelestacia.lelenimexml.core.network.di

import android.viewbinding.library.BuildConfig
import com.lelestacia.lelenimexml.core.network.INetworkDataSource
import com.lelestacia.lelenimexml.core.network.NetworkDataSource
import com.lelestacia.lelenimexml.core.network.source.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLogger() =
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
        } else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

    @Provides
    @Singleton
    fun provideOkHttp(logger: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build().create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideDataSource(apiService: ApiService): INetworkDataSource {
        return NetworkDataSource(apiService)
    }
}