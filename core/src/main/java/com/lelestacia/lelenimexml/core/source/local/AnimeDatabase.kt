package com.lelestacia.lelenimexml.core.source.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lelestacia.lelenimexml.core.model.converter.DateConverter
import com.lelestacia.lelenimexml.core.model.converter.StringConverter
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.core.model.local.SeasonAnimeEntity
import com.lelestacia.lelenimexml.core.model.local.SeasonAnimeKeyEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.local.character.CharacterEntity

@Database(
    entities = [SeasonAnimeEntity::class,
        SeasonAnimeKeyEntity::class,
        AnimeEntity::class,
        CharacterEntity::class,
        CharacterAdditionalInformationEntity::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(value = [StringConverter::class, DateConverter::class])
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun seasonAnimeDao(): SeasonAnimeDao
    abstract fun seasonAnimeKeyDao(): SeasonAnimeKeyDao
    abstract fun animeDao(): AnimeDao
    abstract fun characterDao(): CharacterDao
}