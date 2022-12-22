package com.lelestacia.lelenimexml.feature.anime.domain.util

import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse
import com.lelestacia.lelenimexml.core.utility.Constant.UNKNOWN
import com.lelestacia.lelenimexml.feature.anime.domain.model.Anime
import java.util.*

object AnimeMapperUtil {

    fun networkToAnime(networkData: AnimeResponse): Anime {
        return Anime(
            malID = networkData.malId,
            coverImages = networkData.images.webp.largeImageUrl,
            trailer =
            if (networkData.trailer == null) null
            else Anime.Trailer(
                youtubeId = networkData.trailer?.youtubeId,
                url = networkData.trailer?.url,
                images = networkData.trailer?.images?.largeImageUrl
            ),
            title = networkData.title,
            titleEnglish = networkData.titleEnglish,
            titleJapanese = networkData.titleJapanese,
            type = networkData.type ?: UNKNOWN,
            episodes = networkData.episodes,
            status = networkData.status,
            rating = networkData.rating ?: "",
            score = networkData.score,
            scoredBy = networkData.scoredBy,
            rank = networkData.rank,
            synopsis = networkData.synopsis,
            season = networkData.season,
            year = networkData.year,
            genres = networkData.genres.map {
                it.name
            },
            isFavorite = false
        )
    }

    fun animeToEntity(anime: Anime, isFavorite: Boolean): AnimeEntity {
        return AnimeEntity(
            malId = anime.malID,
            coverImages = anime.coverImages,
            trailer = AnimeEntity.Trailer(
                youtubeId = anime.trailer?.youtubeId,
                url = anime.trailer?.url,
                images = anime.trailer?.images
            ),
            title = anime.title,
            titleEnglish = anime.titleEnglish,
            titleJapanese = anime.titleJapanese,
            type = anime.type,
            episodes = anime.episodes,
            status = anime.status,
            rating = anime.rating,
            score = anime.score,
            scoredBy = anime.scoredBy,
            rank = anime.rank,
            synopsis = anime.synopsis,
            season = anime.season,
            year = anime.year,
            genres = anime.genres,
            lastViewed = Date(),
            isFavorite = isFavorite
        )
    }

    fun animeEntityToAnime(animeEntity: AnimeEntity): Anime {
        return Anime(
            malID = animeEntity.malId,
            coverImages = animeEntity.coverImages,
            trailer = Anime.Trailer(
                youtubeId = animeEntity.trailer?.youtubeId,
                url = animeEntity.trailer?.url,
                images = animeEntity.trailer?.images
            ),
            title = animeEntity.title,
            titleEnglish = animeEntity.titleEnglish,
            titleJapanese = animeEntity.titleJapanese,
            type = animeEntity.type,
            episodes = animeEntity.episodes,
            status = animeEntity.status,
            rating = animeEntity.rating,
            score = animeEntity.score,
            scoredBy = animeEntity.scoredBy,
            rank = animeEntity.rank,
            synopsis = animeEntity.synopsis,
            season = animeEntity.season,
            year = animeEntity.year,
            genres = animeEntity.genres,
            isFavorite = animeEntity.isFavorite
        )
    }
}