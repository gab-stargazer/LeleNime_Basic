package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import com.lelestacia.lelenimexml.core.network.model.GenericRecommendationResponse

fun GenericRecommendationResponse.asRecommendation() : Recommendation =
    Recommendation(
        malID = malID,
        entry = entry.map { it.asGenericModel() },
        content = content,
        date = date,
        userName = user.userName
    )