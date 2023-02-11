package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.model.GenericModel
import com.lelestacia.lelenimexml.core.network.model.GenericModelResponse

fun GenericModelResponse.asGenericModel() =
    GenericModel(
        malID = malID,
        images = images.webp.largeImageURL,
        title = title
    )