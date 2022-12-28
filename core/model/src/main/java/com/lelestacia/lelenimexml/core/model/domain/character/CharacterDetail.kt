package com.lelestacia.lelenimexml.core.model.domain.character

data class CharacterDetail(
    val characterMalId: Int,
    val name: String,
    val characterKanjiName: String,
    val characterNickNames: List<String>,
    val images: String,
    val role: String,
    val favoriteBy: Int,
    val characterInformation: String
)