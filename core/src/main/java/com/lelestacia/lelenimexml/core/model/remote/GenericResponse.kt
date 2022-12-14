package com.lelestacia.lelenimexml.core.model.remote

import com.google.gson.annotations.SerializedName

data class GenericResponse <T>(
    @SerializedName("data")
    val data: T
)
