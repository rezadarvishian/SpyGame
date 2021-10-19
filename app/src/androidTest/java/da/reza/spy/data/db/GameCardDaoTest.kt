package da.reza.spy.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import da.reza.spy.data.model.GameCardItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class GameCardDaoTest {


    /** Explicitly tell Junit to execute all testCase in same thread
     * .... used when test concurrency
     */

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var gameCardDataBase: GameCardDataBase
    private lateinit var gameCardDao: GameCardDao


    /** for all testCase this fun be called first
     * .... To separate the dependencies of the tests on each other
     * ... if any test failed , setUpTest fun called again for next Test*/

    @Before
    fun setUpTest() {
        gameCardDataBase = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            GameCardDataBase::class.java
        ).build()
        gameCardDao = gameCardDataBase.GameCardDao()
    }


    /** for all testCase this fun be called last
     * .... To separate the dependencies of the tests on each other
     * ... finishTest close DataBase For prevent memoryLeaks*/

    @After
    fun finishTest() {
        gameCardDataBase.close()

    }


    @Test
    fun insertGameCard(): Unit = runBlocking {
        val gameCardItem =
            listOf(GameCardItem(id = 1, cardName = "cardGame", cardImage = "imageUrl",false))
        gameCardDao.insertGameCard(gameCardItem.first())

        val gameCards = async {
            gameCardDao.getAllGameCard()
                .take(1)
                .toList()
        }
        assertThat(gameCards.await()).containsExactly(gameCardItem)

    }

    @Test
    fun deleteGameCard(): Unit = runBlocking {
        val gameCardsList = listOf(
            GameCardItem(id = 1, cardName = "cardGame1", cardImage = "imageUrl1" , false),
            GameCardItem(id = 2, cardName = "cardGame2", cardImage = "imageUrl2",false)
        )
        gameCardDao.insertGameCard(gameCardsList[0])
        gameCardDao.insertGameCard(gameCardsList[1])
        gameCardDao.deleteGameCard(gameCardsList[0])
        val gameCards = async {
            gameCardDao.getAllGameCard().take(1).toList()
        }
        assertThat(gameCards.await()).containsExactly(listOf(gameCardsList[1]))
    }


}