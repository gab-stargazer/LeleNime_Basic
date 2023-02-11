package com.lelestacia.lelenimexml.core.network.model.pagination

import com.google.gson.annotations.SerializedName

data class GenericScrappingPaginationResponse<T>(
    @SerializedName("pagination")
    val scrapPagination: ScrappingPaginationDTO,

    @SerializedName("data")
    val data: List<T>
) {
    data class ScrappingPaginationDTO(

        @SerializedName("last_visible_page")
        val lastVisiblePage: Int,

        @SerializedName("has_next_page")
        val hasNextPage: Boolean
    )
}
