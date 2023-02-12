package com.lelestacia.lelenimexml.core.model.recommendation

import com.lelestacia.lelenimexml.core.model.GenericModel

data class Recommendation(
    val malID: String,
    val entry: List<GenericModel>,
    val content: String,
    val date: String,
    val userName: String
)
