package com.lelestacia.lelenimexml.feature_anime.di

import com.lelestacia.lelenimexml.core.repository.AnimeRepository
import com.lelestacia.lelenimexml.core.repository.CharacterRepository
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeInteractor
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeUseCases
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.CharacterInteractor
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.CharacterUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorModule {

    @Provides
    fun provideAnimeInteractor(animeRepository: AnimeRepository): AnimeUseCases =
        AnimeInteractor(animeRepository)

    @Provides
    fun provideCharacterInteractor(characterRepository: CharacterRepository): CharacterUseCases =
        CharacterInteractor(characterRepository)
}