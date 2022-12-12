package com.lelestacia.lelenimexml.core.utility

import com.lelestacia.lelenimexml.core.model.local.SeasonAnimeEntity
import com.lelestacia.lelenimexml.core.model.remote.anime.AnimeResponse

object CoreMapper {

    fun networkToEntities(networkData: AnimeResponse): SeasonAnimeEntity {
        return SeasonAnimeEntity(
            malId = networkData.malId,
            coverImages = networkData.images.webp.largeImageUrl,
            trailer = SeasonAnimeEntity.Trailer(
                youtubeId = networkData.trailer?.youtubeId,
                url = networkData.trailer?.url,
                images = networkData.trailer?.images?.largeImageUrl
            ),
            title =networkData.title,
            titleEnglish = networkData.titleEnglish,
            titleJapanese = networkData.titleJapanese,
            type = networkData.type,
            episodes = networkData.episodes,
            status = networkData.status,
            rating = networkData.rating,
            score = networkData.score,
            scoredBy =  networkData.scoredBy,
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