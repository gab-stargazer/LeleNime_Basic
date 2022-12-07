package com.lelestacia.lelenimexml.core.domain.dto

import com.google.gson.annotations.SerializedName

data class GenericPagingResponse<T>(

    @SerializedName("pagination")
    val pagination: Pagination,

    @SerializedName("data")
    val data: T
)
