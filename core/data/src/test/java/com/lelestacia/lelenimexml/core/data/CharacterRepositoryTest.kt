package com.lelestacia.lelenimexml.core.data

import com.lelestacia.lelenimexml.core.data.impl.character.CharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.character.ICharacterRepository
import com.lelestacia.lelenimexml.core.data.utility.JikanErrorParserUtil
import com.lelestacia.lelenimexml.core.database.service.ICharacterDatabaseService
import com.lelestacia.lelenimexml.core.network.impl.character.ICharacterNetworkService
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(BlockJUnit4ClassRunner::class)
class CharacterRepositoryTest {

    @MockK
    lateinit var apiService: ICharacterNetworkService
    @MockK
    lateinit var databaseService: ICharacterDatabaseService
    private lateinit var characterRepository: ICharacterRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        characterRepository = CharacterRepository(
            apiService = apiService,
            characterDatabaseService = databaseService,
            ioDispatcher = dispatcher,
            errorParser = JikanErrorParserUtil()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
