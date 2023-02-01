package com.lelestacia.lelenimexml.core.database.entity.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lelestacia.lelenimexml.core.database.entity.Constant.CHARACTER_ID
import com.lelestacia.lelenimexml.core.database.entity.Constant.CREATED_AT
import com.lelestacia.lelenimexml.core.database.entity.Constant.IMAGE
import com.lelestacia.lelenimexml.core.database.entity.Constant.UPDATED_AT
import java.util.*

@Entity(tableName = "character_table")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = CHARACTER_ID)
    val characterID: Int,

    @ColumnInfo(name = IMAGE)
    val image: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "role")
    val role: String,

    @ColumnInfo(name = "favorite")
    val favorite: Int,

    @ColumnInfo(name = CREATED_AT)
    val createdAt: Date,

    @ColumnInfo(name = UPDATED_AT)
    val updatedAt: Date?,
)
