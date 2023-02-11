package com.lelestacia.lelenimexml.core.network.model.anime

import com.google.gson.annotations.SerializedName
import com.lelestacia.lelenimexml.core.network.model.GenericModelResponse

data class AnimeRecommendationResponse(

    @SerializedName("entry")
    val entry: GenericModelResponse,

    @SerializedName("votes")
    val votes: Int
)
