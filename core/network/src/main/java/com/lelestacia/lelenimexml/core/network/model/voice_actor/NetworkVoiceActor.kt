package com.lelestacia.lelenimexml.core.network.model.voice_actor

import com.google.gson.annotations.SerializedName

data class NetworkVoiceActor(
    @SerializedName("person")
    val person: Person,

    @SerializedName("language")
    val language: String
) {
    data class Person(
        @SerializedName("mal_id")
        val malID: Int,

        @SerializedName("images")
        val image: NetworkVoiceActorImage,

        @SerializedName("name")
        val name: String,
    ) {
        data class NetworkVoiceActorImage(
            @SerializedName("jpg")
            val jpg: Jpeg
        ) {
            data class Jpeg(
                @SerializedName("image_url")
                val imageUrl: String
            )
        }
    }
}