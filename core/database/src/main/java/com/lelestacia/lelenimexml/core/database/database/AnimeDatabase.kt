package com.lelestacia.lelenimexml.core.database.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.dao.CharacterDao
import com.lelestacia.lelenimexml.core.database.dao.EpisodeDao
import com.lelestacia.lelenimexml.core.database.model.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.database.model.converter.DateConverter
import com.lelestacia.lelenimexml.core.database.model.converter.StringConverter
import com.lelestacia.lelenimexml.core.database.model.episode.EpisodeEntity

@Database(
    entities = [
        AnimeEntity::class,
        CharacterEntity::class,
        CharacterInformationEntity::class,
        EpisodeEntity::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
@TypeConverters(value = [StringConverter::class, DateConverter::class])
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun characterDao(): CharacterDao
    abstract fun episodeDao(): EpisodeDao
}
