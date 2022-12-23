package com.lelestacia.lelenimexml.feature.anime.domain.model

data class Character(
    val characterMalId: Int,
    val images: String,
    val name: String,
    val role: String,
    val favoriteBy: Int
)