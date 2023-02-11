package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.database.entity.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.network.model.anime.AnimeResponse
import java.util.Date

fun AnimeResponse.asAnime(): Anime =
    Anime(
        malID = malId,
        coverImages = coverImages.webp.largeImageUrl,
        trailer = Anime.Trailer(
            youtubeId = trailer?.youtubeId,
            url = trailer?.url,
            images = trailer?.images?.largeImageUrl
        ),
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        type = type ?: "",
        episodes = episodes,
        status = status,
        rating = rating ?: "",
        score = score,
        scoredBy = scoredBy,
        rank = rank,
        synopsis = synopsis,
        season = season,
        year = year,
        genres = genres.map {
            it.name
        },
        isFavorite = false,
        startedDate = aired.from,
        finishedDate = aired.to,
        source = source,
        airing = airing,
        duration = duration,
        studios = studio.map { it.name }
    )

fun AnimeEntity.asAnime(): Anime = Anime(
    malID = animeID,
    coverImages = image,
    trailer = Anime.Trailer(
        youtubeId = trailer?.id,
        url = trailer?.url,
        images = trailer?.image
    ),
    title = title,
    titleEnglish = titleEnglish,
    titleJapanese = titleJapanese,
    type = type,
    episodes = episodes,
    status = status,
    rating = rating,
    score = score,
    scoredBy = scoredBy,
    rank = rank,
    synopsis = synopsis,
    season = season,
    year = year,
    genres = genres,
    isFavorite = isFavorite,
    startedDate = startedDate,
    finishedDate = finishedDate,
    source = source,
    airing = airing,
    duration = duration,
    studios = studios
)

fun Anime.asNewEntity(isFavorite: Boolean = false): AnimeEntity =
    AnimeEntity(
        animeID = malID,
        image = coverImages,
        trailer = AnimeEntity.Trailer(
            id = trailer?.youtubeId,
            url = trailer?.url,
            image = trailer?.images,
        ),
        title = title,
        titleEnglish = titleEnglish,
        titleJapanese = titleJapanese,
        type = type,
        episodes = episodes,
        status = status,
        rating = rating,
        score = score,
        scoredBy = scoredBy,
        rank = rank,
        synopsis = synopsis,
        season = season,
        year = year,
        genres = genres,
        lastViewed = Date(),
        isFavorite = isFavorite,
        startedDate = startedDate,
        finishedDate = finishedDate,
        createdAt = Date(),
        updatedAt = null,
        source = source,
        airing = airing,
        duration = duration,
        studios = studios
    )
