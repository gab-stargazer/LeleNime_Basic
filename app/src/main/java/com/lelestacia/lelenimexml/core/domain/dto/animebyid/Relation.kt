package com.lelestacia.lelenimexml.core.domain.dto.animebyid


import com.google.gson.annotations.SerializedName

data class Relation(
    @SerializedName("entry")
    val entry: List<Entry> = listOf(),
    @SerializedName("relation")
    val relation: String = ""
)