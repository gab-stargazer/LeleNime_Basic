package com.lelestacia.lelenimexml.core.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lelestacia.lelenimexml.core.local.converter.StringConverter
import com.lelestacia.lelenimexml.core.local.dao.SeasonAnimeDao
import com.lelestacia.lelenimexml.core.local.dao.SeasonAnimeKeyDao
import com.lelestacia.lelenimexml.core.local.model.SeasonAnimeEntity
import com.lelestacia.lelenimexml.core.local.model.SeasonAnimeKeyEntity

@Database(
    entities = [SeasonAnimeEntity::class, SeasonAnimeKeyEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(value = [StringConverter::class])
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun seasonAnimeDao(): SeasonAnimeDao
    abstract fun seasonAnimeKeyDao(): SeasonAnimeKeyDao
}