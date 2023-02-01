package com.lelestacia.lelenimexml.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lelestacia.lelenimexml.core.database.entity.character.CharacterVoiceActorCrossRefEntity

@Dao
interface CharacterVoiceActorCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCrossRef(crossRef: List<CharacterVoiceActorCrossRefEntity>)

    @Query("SELECT * FROM character_voice_actor_reference_table WHERE character_id =:characterID")
    fun getVoiceActorsByCharacterID(characterID: Int): List<CharacterVoiceActorCrossRefEntity>
}
