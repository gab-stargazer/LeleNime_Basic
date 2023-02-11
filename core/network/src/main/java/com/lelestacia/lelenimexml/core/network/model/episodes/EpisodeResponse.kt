package com.lelestacia.lelenimexml.core.network.model.episodes

import com.google.gson.annotations.SerializedName

data class EpisodeResponse(
    @SerializedName("mal_id")
    val malID: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("title_japanese")
    val titleJapanese: String?,

    @SerializedName("title_romanji")
    val titleRomanji: String?,

    @SerializedName("aired")
    val aired: String?,

    @SerializedName("score")
    val score: Double,

    @SerializedName("filler")
    val filler: Boolean,

    @SerializedName("recap")
    val recap: Boolean,

    @SerializedName("forum_url")
    val forumURL: String?
)
