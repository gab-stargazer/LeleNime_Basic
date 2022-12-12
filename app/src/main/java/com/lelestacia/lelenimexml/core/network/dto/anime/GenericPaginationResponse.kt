package com.lelestacia.lelenimexml.core.network.dto.anime

import com.google.gson.annotations.SerializedName

data class GenericPaginationResponse<T>(
    @SerializedName("pagination")
    val pagination: PaginationResponse,
    @SerializedName("data")
    val data: T
)