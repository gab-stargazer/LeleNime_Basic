package com.lelestacia.lelenimexml.core.domain.dto.season


import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("current_page")
    val currentPage: Int = 0,
    @SerializedName("has_next_page")
    val hasNextPage: Boolean = false,
    @SerializedName("items")
    val items: Items = Items(),
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int = 0
)