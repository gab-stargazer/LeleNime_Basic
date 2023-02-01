package com.lelestacia.lelenimexml.core.model.episode

data class Episode(
    val malID: Int,
    val animeID: Int,
    val title: String,
    val titleJapanese: String?,
    val titleRomanji: String?,
    val aired: String?,
    val score: Double,
    val filler: Boolean,
    val recap: Boolean,
    val forumURL: String?
)
