package com.lelestacia.lelenimexml.feature_anime.utility

import com.lelestacia.lelenimexml.core.network.dto.anime.AnimeResponse
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime

object AnimeMapper {

    fun networkToAnime(networkData: AnimeResponse): Anime {
        return Anime(
            malId = networkData.malId,
            images = networkData.images.webp.largeImageUrl,
            trailer =
            if (networkData.trailer == null) null
            else Anime.Trailer(
                youtubeId = networkData.trailer.youtubeId,
                url = networkData.trailer.url,
                images = networkData.trailer.images?.largeImageUrl
            ),
            title = networkData.title,
            titleEnglish = networkData.titleEnglish,
            titleJapanese = networkData.titleJapanese,
            type = networkData.type,
            episodes = networkData.episodes,
            status = networkData.status,
            rating = networkData.rating,
            score = networkData.score,
            scoredBy = networkData.scoredBy,
            rank = networkData.rank,
            synopsis = networkData.synopsis,
            season = networkData.season,
            year = networkData.year,
            genres = networkData.genres.map {
                it.name
            }
        )
    }
}