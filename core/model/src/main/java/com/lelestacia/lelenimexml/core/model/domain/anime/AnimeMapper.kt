package com.lelestacia.lelenimexml.core.model.domain.anime

import com.lelestacia.lelenimexml.core.model.database.AnimeEntity
import com.lelestacia.lelenimexml.core.model.network.anime.NetworkAnime
import java.util.Date

fun NetworkAnime.asAnime(): Anime =
    Anime(
        malID = malId,
        coverImages = images.webp.largeImageUrl,
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
        isFavorite = false
    )

fun AnimeEntity.asAnime(): Anime = Anime(
    malID = malId,
    coverImages = coverImages,
    trailer = Anime.Trailer(
        youtubeId = trailer?.youtubeId,
        url = trailer?.url,
        images = trailer?.images
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
    isFavorite = isFavorite
)

fun Anime.asEntity(isFavorite: Boolean = false): AnimeEntity = AnimeEntity(
    malId = malID,
    coverImages = coverImages,
    trailer = AnimeEntity.Trailer(
        youtubeId = trailer?.youtubeId,
        url = trailer?.url,
        images = trailer?.images,
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
    isFavorite = isFavorite
)