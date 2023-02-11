package com.lelestacia.lelenimexml.core.network.model.manga

import com.google.gson.annotations.SerializedName
import com.lelestacia.lelenimexml.core.network.model.GenericModelResponse

data class MangaRecommendationResponse(
    @SerializedName("entry")
    val entry: GenericModelResponse
)
