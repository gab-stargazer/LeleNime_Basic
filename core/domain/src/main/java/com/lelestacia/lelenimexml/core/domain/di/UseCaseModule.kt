package com.lelestacia.lelenimexml.core.domain.di

import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.character.ICharacterRepository
import com.lelestacia.lelenimexml.core.domain.usecase.AnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.CharacterUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.IAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.ICharacterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideAnimeUseCase(repository: IAnimeRepository): IAnimeUseCase =
        AnimeUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideCharacterUseCase(repository: ICharacterRepository): ICharacterUseCase =
        CharacterUseCase(repository)
}
