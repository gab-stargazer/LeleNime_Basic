package com.lelestacia.lelenimexml.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lelestacia.lelenimexml.core.model.converter.DateConverter
import com.lelestacia.lelenimexml.core.model.converter.StringConverter
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.core.model.local.CharacterEntity
import com.lelestacia.lelenimexml.core.model.local.SeasonAnimeEntity
import com.lelestacia.lelenimexml.core.model.local.SeasonAnimeKeyEntity

@Database(
    entities = [SeasonAnimeEntity::class, SeasonAnimeKeyEntity::class, AnimeEntity::class, CharacterEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(value = [StringConverter::class, DateConverter::class])
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun seasonAnimeDao(): SeasonAnimeDao
    abstract fun seasonAnimeKeyDao(): SeasonAnimeKeyDao
    abstract fun animeDao(): AnimeDao
    abstract fun characterDao(): CharacterDao
}