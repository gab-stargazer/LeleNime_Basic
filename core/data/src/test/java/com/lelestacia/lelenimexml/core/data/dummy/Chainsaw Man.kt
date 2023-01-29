package com.lelestacia.lelenimexml.core.data.dummy

import com.lelestacia.lelenimexml.core.data.utility.asEntity
import com.lelestacia.lelenimexml.core.database.model.anime.AnimeEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterEntity
import com.lelestacia.lelenimexml.core.database.model.character.CharacterProfile
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacter
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacterDetail
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacterImage
import java.util.Date

val chainsawManEntity = AnimeEntity(
    animeID = 44511,
    coverImages = "https://cdn.myanimelist.net/images/anime/1806/126216l.jpg",
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
    isFavorite = false,
    lastViewed = Date(),
    startedDate = Date().toString(),
    finishedDate = Date().toString(),
    createdAt = Date(),
    updatedAt = Date()
)

val chainsawManCharacters = listOf(
    NetworkCharacter(
        characterData = NetworkCharacter.CharacterResponseData(
            characterMalId = 170732,
            images = NetworkCharacterImage(
                NetworkCharacterImage.Webp(
                    imageUrl = "https://cdn.myanimelist.net/images/characters/3/492407.webp?s=4d8e9a01dac6a5d891c3ed434187fba9"
                )
            ),
            name = "Denji"
        ),
        role = "Main",
        favoriteBy = 13968
    ),
    NetworkCharacter(
        characterData = NetworkCharacter.CharacterResponseData(
            characterMalId = 170735,
            images = NetworkCharacterImage(
                NetworkCharacterImage.Webp(
                    imageUrl = "https://cdn.myanimelist.net/images/characters/10/492791.webp?s=bcd7cca0e5126478eb2417666188adc7"
                )
            ),
            name = "Hayakawa, Aki"
        ),
        role = "Main",
        favoriteBy = 6872
    ),
    NetworkCharacter(
        characterData = NetworkCharacter.CharacterResponseData(
            characterMalId = 170733,
            images = NetworkCharacterImage(
                NetworkCharacterImage.Webp(
                    imageUrl = "https://cdn.myanimelist.net/images/characters/7/494969.webp?s=02622d6ffa487b8d6d7503af4792a106"
                )
            ),
            name = "Power"
        ),
        role = "Main",
        favoriteBy = 18586
    )
)

val powerCharacterDetail = NetworkCharacterDetail(
    characterMalId = 170733,
    characterName = "Power",
    characterKanjiName = "パワー",
    characterNickNames = listOf("Blood Devil"),
    characterFavoriteCount = 19730,
    characterInformation = "Power is a fiend and Public Safety Devil Hunter, part of Makima's squad. She's a work partner and good friend with Denji. They share an apartment alongside Aki Hayakawa. She lacks manners and often lies to avoid punishment. She's very fond of her pet cat Nyako to the point she was willing to sacrifice Denji's life to save Nyako who had been captured by a bat demon.\n\n(Source: Chainsaw Man Wiki)"
)

val powerProfile = CharacterProfile(
    character = CharacterEntity(
        characterID = 170733,
        animeID = 44511,
        characterName = "Power",
        characterImage = "https://cdn.myanimelist.net/images/characters/7/494969.webp?s=02622d6ffa487b8d6d7503af4792a106",
        characterRole = "Main",
        characterFavorite = 19730,
        createdAt = Date(),
        updatedAt = Date()
    ),
    additionalInformation = powerCharacterDetail.asEntity()
)
