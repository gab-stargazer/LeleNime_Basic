package com.lelestacia.lelenimexml.core.model.network

import com.google.gson.annotations.SerializedName

data class GenericPaginationResponse<T>(
    @SerializedName("pagination")
    val pagination: PaginationResponse,
    @SerializedName("data")
    val data: T
)
