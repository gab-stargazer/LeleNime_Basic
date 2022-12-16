package com.lelestacia.lelenimexml.core.model.local.character

import androidx.room.Entity

@Entity(
    tableName = "character_additional_information_table",
    primaryKeys = ["characterId", "characterKanjiName"]
)
data class CharacterAdditionalInformationEntity(
    val characterId: Int,
    val characterKanjiName: String,
    val characterNickNames: List<String>,
    val characterFavoriteCount: Int,
    val characterInformation: String
)