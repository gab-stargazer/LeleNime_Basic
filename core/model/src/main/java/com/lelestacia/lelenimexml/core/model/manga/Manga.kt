package com.lelestacia.lelenimexml.core.model.manga

data class Manga(
    val malID: Int,
    val images: String,
    val approved: Boolean,
    val title: String,
    val titleEnglish: String?,
    val titleJapanese: String?,
    val chapters: Int,
    val volume: Int,
    val status: String,
    val publishing: Boolean,
    val score: Double?,
    val scoredBy: Double?,
    val rank: Int,
    val synopsis: String
)
