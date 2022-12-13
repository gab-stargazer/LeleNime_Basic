package com.lelestacia.lelenimexml.core.model.remote.animefull


import com.google.gson.annotations.SerializedName

data class AnimeFUll(
    @SerializedName("data")
    val `data`: Data = Data()
)