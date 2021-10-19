package da.reza.spy.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import da.reza.spy.CoroutineRules
import da.reza.spy.repository.FakeGameCardRepository
import da.reza.spy.utiles.BaseResult
import da.reza.spy.utiles.ConstVal
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GameViewModelTest {

    /** Explicitly tell Junit to execute all testCase in same thread
     * .... used when test concurrency
     */

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRules = CoroutineRules()

    private lateinit var viewModel: GameViewModel


    @Before
    fun setUp() {
        viewModel = GameViewModel(FakeGameCardRepository())
    }

    @Test
    fun `insert shopping item with empty Name field, returns error`() {

        viewModel.insertGameCard("", "", true)

        val value = viewModel.insertGameCardStatus.value

        assertThat(value?.status).isEqualTo(BaseResult.Status.ERROR)

    }

    @Test
    fun `insert shopping item with Long Name, returns error`() {

        val longString = buildString {
            repeat(ConstVal.MAX_NAME_LENGTH + 1) {
                append(it)
            }
        }

        viewModel.insertGameCard(longString, "", true)

        val value = viewModel.insertGameCardStatus.value

        assertThat(value?.status).isEqualTo(BaseResult.Status.ERROR)

    }


    @Test
    fun `insert shopping item with valid field, returns success`() = runBlocking {


        async {
            viewModel.insertGameCard("name", "", true)
        }.join()

        val value = viewModel.insertGameCardStatus.value

        assertThat(value?.status).isEqualTo(BaseResult.Status.SUCCESS)

    }


}