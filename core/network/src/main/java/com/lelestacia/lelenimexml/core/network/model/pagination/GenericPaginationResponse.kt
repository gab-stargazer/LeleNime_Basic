package com.lelestacia.lelenimexml.core.network.model.pagination

import com.google.gson.annotations.SerializedName
import com.lelestacia.lelenimexml.core.network.model.PaginationResponse

data class GenericPaginationResponse<T>(
    @SerializedName("pagination")
    val pagination: PaginationResponse,
    @SerializedName("data")
    val data: List<T>
)
