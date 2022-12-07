package com.lelestacia.lelenimexml.feature_anime.domain.model

data class AnimeCard(
    val malID: Int,
    val coverImage: String,
    val title: String,
    val rating: Double,
    val episode: Int,
    val status: String
)
