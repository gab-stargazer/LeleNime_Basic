package com.lelestacia.lelenimexml.core.network.model

import com.google.gson.annotations.SerializedName

data class GenericResponse <T>(
    @SerializedName("data")
    val data: T
)
