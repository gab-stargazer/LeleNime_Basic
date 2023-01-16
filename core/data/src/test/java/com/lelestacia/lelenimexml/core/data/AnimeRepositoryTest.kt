package com.lelestacia.lelenimexml.core.data

import com.lelestacia.lelenimexml.core.data.dummy.chainsawManEntity
import com.lelestacia.lelenimexml.core.database.IAnimeLocalDataSource
import com.lelestacia.lelenimexml.core.database.ICharacterLocalDataSource
import com.lelestacia.lelenimexml.core.database.user_pref.UserPref
import com.lelestacia.lelenimexml.core.network.INetworkDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(BlockJUnit4ClassRunner::class)
class AnimeRepositoryTest {

    @get:Rule
    val mockRule = MockKRule(this)

    @MockK
    lateinit var animeLocalDataSource: IAnimeLocalDataSource

    @MockK
    lateinit var characterLocalDataSource: ICharacterLocalDataSource

    @MockK
    lateinit var networkDataSource: INetworkDataSource

    @MockK
    lateinit var userPref: UserPref

    private lateinit var animeRepository: IAnimeRepository
    private lateinit var characterRepository: ICharacterRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        animeRepository = AnimeRepository(networkDataSource, animeLocalDataSource, userPref)
        characterRepository =
            CharacterRepository(networkDataSource, characterLocalDataSource, Dispatchers.Main)
    }

    @Test
    fun `Anime ID 0 should throw NPE`() = runTest {
        val wrongAnimeID = 0
        coEvery { animeLocalDataSource.getNewestAnimeDataByAnimeId(animeID = wrongAnimeID) } throws NullPointerException()
        assertThrows(
            NullPointerException::class.java
        ) {
            animeRepository.getNewestAnimeDataByAnimeId(animeID = wrongAnimeID)
        }
        coVerify { animeLocalDataSource.getNewestAnimeDataByAnimeId(animeID = wrongAnimeID) }
    }

    @Test
    fun `Anime ID 44511 should return Chainsaw-Man Anime Newest Data`() = runTest {
        val chainsawManID = chainsawManEntity.malId
        val newestData = flowOf(
            chainsawManEntity
        )
        coEvery { animeLocalDataSource.getNewestAnimeDataByAnimeId(chainsawManID) } answers { newestData }
        val result = animeRepository.getNewestAnimeDataByAnimeId(animeID = chainsawManID)
        result.collectLatest {
            assertEquals(
                "Fetching anime ID 44511 should return newest Data of Chainsaw-Man",
                chainsawManEntity,
                it
            )
            coVerify { animeLocalDataSource.getNewestAnimeDataByAnimeId(animeID = chainsawManID) }
        }
    }

    @Test
    fun `Anime ID 0 should return null`() = runTest {
        val wrongAnimeID = 0
        coEvery { animeLocalDataSource.getAnimeByAnimeId(animeID = wrongAnimeID) } answers { null }
        val result = animeRepository.getAnimeByAnimeId(animeID = wrongAnimeID)
        coVerify { animeLocalDataSource.getAnimeByAnimeId(animeID = wrongAnimeID) }
        assertEquals("Fetching Anime ID 0 should return Null", null, result)
    }

    @Test
    fun `Anime ID 44511 should return Chainsaw-Man Anime`() = runTest {
        val chainsawManID = chainsawManEntity.malId
        coEvery { animeLocalDataSource.getAnimeByAnimeId(animeID = chainsawManID) } answers { chainsawManEntity }
        val result = animeRepository.getAnimeByAnimeId(animeID = chainsawManID)
        coVerify { animeLocalDataSource.getAnimeByAnimeId(animeID = chainsawManID) }
        assertEquals(
            "Fetching Anime with ID 44511 should return Chainsaw Man", chainsawManEntity, result
        )
    }

    @Test
    fun `Verify if anime was updated properly`() = runTest {
        val beforeUpdate = chainsawManEntity
        val afterUpdate = beforeUpdate.copy(
            isFavorite = !chainsawManEntity.isFavorite
        )
        assertNotEquals(
            "Favorite should be different",
            beforeUpdate.isFavorite,
            afterUpdate.isFavorite
        )
        coEvery { animeLocalDataSource.updateAnime(afterUpdate) } answers { }
        coEvery { animeLocalDataSource.getAnimeByAnimeId(chainsawManEntity.malId) } answers { beforeUpdate }
        animeRepository.updateAnimeFavorite(chainsawManEntity.malId)
        coVerify { animeLocalDataSource.updateAnime(afterUpdate) }
    }
}