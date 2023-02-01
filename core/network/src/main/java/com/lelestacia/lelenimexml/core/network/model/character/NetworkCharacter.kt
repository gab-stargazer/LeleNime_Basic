package com.lelestacia.lelenimexml.core.network.model.character

import com.google.gson.annotations.SerializedName
import com.lelestacia.lelenimexml.core.network.model.voice_actor.NetworkVoiceActor

data class NetworkCharacter(
    @SerializedName("character")
    val characterData: CharacterResponseData,

    @SerializedName("role")
    val role: String,

    @SerializedName("favorites")
    val favorites: Int = 0,

    @SerializedName("voice_actors")
    val voiceActors: List<NetworkVoiceActor>
) {
    data class CharacterResponseData(
        @SerializedName("mal_id")
        val malID: Int,

        @SerializedName("images")
        val images: NetworkCharacterImage,

        @SerializedName("name")
        val name: String
    ) {
        data class NetworkCharacterImage(
            @SerializedName("webp")
            val webp: Webp
        ) {
            data class Webp(
                @SerializedName("image_url")
                val imageUrl: String
            )
        }
    }
}
