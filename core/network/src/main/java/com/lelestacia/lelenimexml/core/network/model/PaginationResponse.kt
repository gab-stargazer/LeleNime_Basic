package com.lelestacia.lelenimexml.core.network.model

import com.google.gson.annotations.SerializedName

data class PaginationResponse(
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int,
    @SerializedName("has_next_page")
    val hasNextPage: Boolean,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("items")
    val PaginationItem: Items
) {
    data class Items(
        @SerializedName("count")
        val count: Int,
        @SerializedName("total")
        val total: Int,
        @SerializedName("per_page")
        val perPage: Int
    )
}
