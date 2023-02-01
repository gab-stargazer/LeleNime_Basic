package com.lelestacia.lelenimexml.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lelestacia.lelenimexml.core.database.dao.AnimeCharacterCrossRefDao
import com.lelestacia.lelenimexml.core.database.dao.AnimeDao
import com.lelestacia.lelenimexml.core.database.dao.CharacterDao
import com.lelestacia.lelenimexml.core.database.dao.CharacterVoiceActorCrossRefDao
import com.lelestacia.lelenimexml.core.database.dao.EpisodeDao
import com.lelestacia.lelenimexml.core.database.dao.VoiceActorDao
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeCharacterReferenceEntity
import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.database.entity.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.entity.character.CharacterInformationEntity
import com.lelestacia.lelenimexml.core.database.entity.character.CharacterVoiceActorCrossRefEntity
import com.lelestacia.lelenimexml.core.database.entity.episode.EpisodeEntity
import com.lelestacia.lelenimexml.core.database.entity.voice_actor.VoiceActorEntity
import com.lelestacia.lelenimexml.core.database.type_converter.DateConverter
import com.lelestacia.lelenimexml.core.database.type_converter.StringConverter

@Database(
    entities = [
        AnimeEntity::class,
        AnimeCharacterReferenceEntity::class,
        CharacterEntity::class,
        CharacterVoiceActorCrossRefEntity::class,
        CharacterInformationEntity::class,
        EpisodeEntity::class,
        VoiceActorEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(value = [StringConverter::class, DateConverter::class])
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao
    abstract fun animeCharacterCrossRefDao(): AnimeCharacterCrossRefDao
    abstract fun characterDao(): CharacterDao
    abstract fun characterVoiceActorCrossRefDao(): CharacterVoiceActorCrossRefDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun voiceActorDao(): VoiceActorDao
}
