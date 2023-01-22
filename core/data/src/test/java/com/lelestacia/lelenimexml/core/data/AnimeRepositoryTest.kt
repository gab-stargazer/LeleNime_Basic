package com.lelestacia.lelenimexml.core.data

import android.content.Context
import com.lelestacia.lelenimexml.core.data.impl.anime.AnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.database.IAnimeLocalDataSource
import com.lelestacia.lelenimexml.core.network.INetworkAnimeService
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(BlockJUnit4ClassRunner::class)
class AnimeRepositoryTest {

    @get:Rule
    val mockRule = MockKRule(this)

    @MockK
    lateinit var databaseService: IAnimeLocalDataSource

    @MockK
    lateinit var apiService: INetworkAnimeService

    @MockK
    lateinit var mContext: Context

    private lateinit var animeRepository: IAnimeRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        animeRepository = AnimeRepository(apiService, databaseService, mContext)
    }
}
