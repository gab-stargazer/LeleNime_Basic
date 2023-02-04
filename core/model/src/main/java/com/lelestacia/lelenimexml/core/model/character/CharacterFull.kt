package com.lelestacia.lelenimexml.core.model.character

import com.lelestacia.lelenimexml.core.model.voice_actor.VoiceActor

data class CharacterFull(
    val characterID: Int,
    val name: String,
    val characterKanjiName: String,
    val characterNickNames: List<String>,
    val images: String,
    val role: String,
    val favoriteBy: Int,
    val characterInformation: String,
    val voiceActors: List<VoiceActor>
)