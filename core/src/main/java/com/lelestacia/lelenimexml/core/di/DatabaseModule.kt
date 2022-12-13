package com.lelestacia.lelenimexml.core.di

import android.content.Context
import androidx.room.Room
import com.lelestacia.lelenimexml.core.source.local.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context) : AnimeDatabase =
        Room.databaseBuilder(context, AnimeDatabase::class.java, "anime_db").build()
}