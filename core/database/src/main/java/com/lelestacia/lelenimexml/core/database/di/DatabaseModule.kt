package com.lelestacia.lelenimexml.core.database.di

import android.content.Context
import androidx.core.content.edit
import androidx.room.Room
import com.lelestacia.lelenimexml.core.common.Constant.USER_PREF
import com.lelestacia.lelenimexml.core.database.ILocalDataSource
import com.lelestacia.lelenimexml.core.database.LocalDataSource
import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.dao.CharacterDao
import com.lelestacia.lelenimexml.core.database.database.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePassword(@ApplicationContext context: Context): String {
        val userPref = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        val password = userPref.getString(PASSWORD, "")

        if (password.isNullOrEmpty()) {
            val stringChar = ('0'..'z').toList().toTypedArray()
            val newPassword = (1..32).map { stringChar.random() }.joinToString("")
            userPref.edit {
                putString(PASSWORD, newPassword)
                apply()
            }
            return newPassword
        }
        return password
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context, password: String): AnimeDatabase =
        Room
            .databaseBuilder(
                context,
                AnimeDatabase::class.java,
                "anime.db"
            )
            .openHelperFactory(SupportFactory(SQLiteDatabase.getBytes(password.toCharArray())))
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideAnimeDao(animeDatabase: AnimeDatabase): AnimeDao =
        animeDatabase.animeDao()

    @Provides
    @Singleton
    fun provideCharacterDao(animeDatabase: AnimeDatabase): CharacterDao =
        animeDatabase.characterDao()

    @Provides
    @Singleton
    fun provideLocalDataSource(animeDao: AnimeDao, characterDao: CharacterDao): ILocalDataSource =
        LocalDataSource(animeDao, characterDao)

    private const val PASSWORD = "password"
}
