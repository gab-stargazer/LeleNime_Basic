package com.lelestacia.lelenimexml.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.dao.CharacterDao
import com.lelestacia.lelenimexml.core.database.dao.SeasonAnimeDao
import com.lelestacia.lelenimexml.core.database.dao.SeasonAnimeKeyDao
import com.lelestacia.lelenimexml.core.model.database.AnimeEntity
import com.lelestacia.lelenimexml.core.model.database.SeasonAnimeEntity
import com.lelestacia.lelenimexml.core.model.database.SeasonAnimeKeyEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterAdditionalInformationEntity
import com.lelestacia.lelenimexml.core.model.database.character.CharacterEntity
import com.lelestacia.lelenimexml.core.model.database.converter.DateConverter
import com.lelestacia.lelenimexml.core.model.database.converter.StringConverter

@Database(
    entities = [
        SeasonAnimeEntity::class,
        SeasonAnimeKeyEntity::class,
        AnimeEntity::class,
        CharacterEntity::class,
        CharacterAdditionalInformationEntity::class],
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