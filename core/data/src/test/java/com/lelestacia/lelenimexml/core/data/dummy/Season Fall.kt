package com.lelestacia.lelenimexml.core.data.dummy

import com.lelestacia.lelenimexml.core.network.model.anime.AnimeResponse
import java.util.Date

val chainsawManNetwork = AnimeResponse(
    malId = 44511,
    coverImages = AnimeResponse.AnimeImagesDTO(
        webp = AnimeResponse.AnimeImagesDTO.Webp(
            imageUrl = "https://cdn.myanimelist.net/images/anime/1806/126216l.jpg",
            largeImageUrl = "https://cdn.myanimelist.net/images/anime/1806/126216l.jpg"
        )
    ),
    trailer = null,
    title = "Chainsaw Man",
    titleEnglish = "Chainsaw Man",
    titleJapanese = "チェンソーマン",
    type = "TV",
    episodes = 12,
    status = "Finished Airing",
    rating = "R - 17+ (violence & profanity)",
    score = 8.72,
    scoredBy = 363500,
    rank = 45,
    synopsis = "Denji is robbed of a normal teenage life, left with nothing but his deadbeat father's overwhelming debt. His only companion is his pet, the chainsaw devil Pochita, with whom he slays devils for money that inevitably ends up in the yakuza's pockets. All Denji can do is dream of a good, simple life: one with delicious food and a beautiful girlfriend by his side. But an act of greedy betrayal by the yakuza leads to Denji's brutal, untimely death, crushing all hope of him ever achieving happiness.\n\nRemarkably, an old contract allows Pochita to merge with the deceased Denji and bestow devil powers on him, changing him into a hybrid able to transform his body parts into chainsaws. Because Denji's new abilities pose a significant risk to society, the Public Safety Bureau's elite devil hunter Makima takes him in, letting him live as long as he obeys her command. Guided by the promise of a content life alongside an attractive woman, Denji devotes everything and fights with all his might to make his naive dreams a reality.\n\n[Written by MAL Rewrite]",
    season = "fall",
    year = 2022,
    genres = listOf(),
    aired = AnimeResponse.AnimeAiringInformationResponse(
        from = Date().toString(),
        to = Date().toString(),
    ),
    source = "Manga",
    airing = false,
    duration = "",
    studio = emptyList()
)

val mobPsychoNetwork = AnimeResponse(
    malId = 50172,
    coverImages = AnimeResponse.AnimeImagesDTO(
        webp = AnimeResponse.AnimeImagesDTO.Webp(
            imageUrl = "https://cdn.myanimelist.net/images/anime/1228/125011.jpg",
            largeImageUrl = "https://cdn.myanimelist.net/images/anime/1228/125011.jpg"
        )
    ),
    trailer = null,
    title = "Mob Psycho 100 III",
    titleEnglish = "Mob Psycho 100 III",
    titleJapanese = "モブサイコ100 III",
    type = "TV",
    episodes = 12,
    status = "Finished Airing",
    rating = "PG-13 - Teens 13 or older",
    score = 8.76,
    scoredBy = 143325,
    rank = 36,
    synopsis = "After foiling a world-threatening plot, Shigeo \"Mob\" Kageyama returns to tackle the more exhausting aspects of his mundane life—starting with filling out his school's nerve-racking career form. Meanwhile, he continues to assist his mentor Arataka Reigen and the office's new recruit, Katsuya Serizawa, in solving paranormal cases of their clients. While continuing his duties, Mob also works on gaining more independence in his esper and human lives, as well as trying to integrate better with the people around him.\n\nHowever, new supernatural and ordinary challenges test Mob’s emotional stability and force him to confront the realities around him. As he strives to continue forward on the path to maturity, Mob must resolve his emotional crises and reassess the naivety he has held on for so long.\n\n[Written by MAL Rewrite]",
    season = "fall",
    year = 2022,
    genres = listOf(),
    aired = AnimeResponse.AnimeAiringInformationResponse(
        from = Date().toString(),
        to = Date().toString(),
    ),
    source = "Manga",
    airing = false,
    duration = "",
    studio = emptyList()
)
