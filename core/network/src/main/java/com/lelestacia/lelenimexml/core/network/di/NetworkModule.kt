package com.lelestacia.lelenimexml.core.network.di

import android.content.Context
import com.lelestacia.lelenimexml.core.network.INetworkAnimeService
import com.lelestacia.lelenimexml.core.network.INetworkCharacterService
import com.lelestacia.lelenimexml.core.network.NetworkAnimeService
import com.lelestacia.lelenimexml.core.network.NetworkCharacterService
import com.lelestacia.lelenimexml.core.network.source.ApiService
import com.lelestacia.lelenimexml.core.network.source.ApiService.Companion.HOSTNAME
import com.lelestacia.lelenimexml.core.network.util.ConnectivityChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSSL(): CertificatePinner = CertificatePinner.Builder()
        .add(HOSTNAME, "sha256/WxVeH3behrxKvQkDq0Rk1d7c8ZFEx/rxNV4XNhHszo8=")
        .add(HOSTNAME, "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=")
        .add(HOSTNAME, "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=").build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)

    @Provides
    @Singleton
    @Named(ONLINE_INTERCEPTOR)
    fun provideOnlineInterceptor(): Interceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 60 * 60
        response
            .newBuilder()
            .header(CACHE_CONTROL, "public, max-age=$maxAge")
            .removeHeader(PRAGMA)
            .build()
    }

    @Provides
    @Singleton
    @Named(OFFLINE_INTERCEPTOR)
    fun provideOfflineInterceptor(@ApplicationContext mContext: Context): Interceptor =
        Interceptor { chain ->
            var request = chain.request()
            val connectivityChecker = ConnectivityChecker()
            val isConnectivityAvailable = connectivityChecker(mContext)
            if (!isConnectivityAvailable) {
                val maxStale = 60 * 60 * 24 * 7
                request = request
                    .newBuilder()
                    .header(CACHE_CONTROL, "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader(PRAGMA)
                    .build()
            }
            chain.proceed(request)
        }

    @Provides
    @Singleton
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named(ONLINE_INTERCEPTOR) internetInterceptor: Interceptor,
        @Named(OFFLINE_INTERCEPTOR) offlineInterceptor: Interceptor,
        shaKey: CertificatePinner,
        @ApplicationContext mContext: Context
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .cache(Cache(mContext.cacheDir, CACHE_SIZE))
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(internetInterceptor)
            .addInterceptor(loggingInterceptor)
            .certificatePinner(shaKey)
            .build()

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService =
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .validateEagerly(true)
            .build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideAnimeDataSource(apiService: ApiService): INetworkAnimeService =
        NetworkAnimeService(apiService)

    @Provides
    @Singleton
    fun provideCharacterDataSource(apiService: ApiService): INetworkCharacterService =
        NetworkCharacterService(apiService)

    private const val CACHE_SIZE: Long = (50 * 1024 * 1024).toLong()
    private const val ONLINE_INTERCEPTOR = "online_interceptor"
    private const val OFFLINE_INTERCEPTOR = "offline_interceptor"
    private const val CACHE_CONTROL = "Cache-Control"
    private const val PRAGMA = "Pragma"
}
