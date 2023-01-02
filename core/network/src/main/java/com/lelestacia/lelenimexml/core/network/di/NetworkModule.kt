package com.lelestacia.lelenimexml.core.network.di

import android.viewbinding.library.BuildConfig
import com.lelestacia.lelenimexml.core.network.INetworkDataSource
import com.lelestacia.lelenimexml.core.network.NetworkDataSource
import com.lelestacia.lelenimexml.core.network.source.ApiService
import com.lelestacia.lelenimexml.core.network.source.ApiService.Companion.HOSTNAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
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
    fun provideSSL(): CertificatePinner = CertificatePinner
        .Builder()
        .add(HOSTNAME, "sha256/WxVeH3behrxKvQkDq0Rk1d7c8ZFEx/rxNV4XNhHszo8=")
        .add(HOSTNAME, "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=")
        .add(HOSTNAME, "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
        .build()

    @Provides
    @Singleton
    fun provideLogger() =
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
        } else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

    @Provides
    @Singleton
    fun provideOkHttp(logger: HttpLoggingInterceptor, shaKey: CertificatePinner) = OkHttpClient
        .Builder()
        .addInterceptor(logger)
        .certificatePinner(shaKey)
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
