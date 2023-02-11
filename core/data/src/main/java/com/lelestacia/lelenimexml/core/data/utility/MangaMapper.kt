package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.core.network.model.manga.MangaResponse

fun MangaResponse.asManga() =
    Manga(
        malID = malID,
        images = images.webp.largeImageURL,
        approved = approved,
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        chapters = chapters,
        volume = volume,
        status = status,
        publishing = publishing,
        score = score,
        scoredBy = scoredBy,
        rank = rank,
        synopsis = synopsis
    )