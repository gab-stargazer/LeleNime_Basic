package com.lelestacia.lelenimexml.feature_anime.domain.model

data class CharacterFullProfile(
    val characterMalId: Int,
    val name: String,
    val characterKanjiName: String,
    val characterNickNames: List<String>,
    val images: String,
    val role: String,
    val favoriteBy: Int,
    val characterInformation: String
)