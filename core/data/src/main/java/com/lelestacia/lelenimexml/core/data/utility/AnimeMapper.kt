package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.database.model.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.network.model.anime.NetworkAnime
import java.util.Date

fun NetworkAnime.asAnime(): Anime =
    Anime(
        animeID = malId,
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
        isFavorite = false,
        startedDate = aired.from,
        finishedDate = aired.to
    )

fun AnimeEntity.asAnime(): Anime = Anime(
    animeID = animeID,
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
    isFavorite = isFavorite,
    startedDate = startedDate,
    finishedDate = finishedDate
)

fun Anime.asEntity(isFavorite: Boolean = false): AnimeEntity =
    AnimeEntity(
        animeID = animeID,
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
        isFavorite = isFavorite,
        startedDate = startedDate,
        finishedDate = finishedDate
    )
