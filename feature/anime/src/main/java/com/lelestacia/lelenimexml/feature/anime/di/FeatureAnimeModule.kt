package com.lelestacia.lelenimexml.feature.anime.di

import com.lelestacia.lelenimexml.core.repository.AnimeRepository
import com.lelestacia.lelenimexml.core.repository.CharacterRepository
import com.lelestacia.lelenimexml.feature.anime.domain.usecase.AnimeInteractor
import com.lelestacia.lelenimexml.feature.anime.domain.usecase.AnimeUseCase
import com.lelestacia.lelenimexml.feature.anime.domain.usecase.CharacterInteractor
import com.lelestacia.lelenimexml.feature.anime.domain.usecase.CharacterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object FeatureAnimeModule {

    @Provides
    fun provideAnimeInteractor(animeRepository: AnimeRepository): AnimeUseCase =
        AnimeInteractor(animeRepository)

    @Provides
    fun provideCharacterInteractor(characterRepository: CharacterRepository) : CharacterUseCase =
        CharacterInteractor(characterRepository)
}