package com.lelestacia.lelenimexml.core.network.model

import com.google.gson.annotations.SerializedName

data class GenericRecommendationResponse(

    @SerializedName("mal_id")
    val malID: String,

    @SerializedName("entry")
    val entry: List<GenericModelResponse>,

    @SerializedName("content")
    val content: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("user")
    val user: UserRecommendationAnimeResponse
) {

    data class UserRecommendationAnimeResponse(

        @SerializedName("username")
        val userName: String
    )
}