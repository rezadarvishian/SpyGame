package da.reza.spy.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.remote.responses.ImageResponse
import da.reza.spy.utiles.BaseResult
import kotlinx.coroutines.flow.Flow

class FakeGameCardRepository : GameCardRepositoryInterface {


    private val gameCards = mutableListOf<GameCardItem>()
    private val collectGameCards = MutableLiveData<List<GameCardItem>>(gameCards)
    private var networkRequestReturnError = false


    fun setNetworkRequestReturnError(state:Boolean){
        networkRequestReturnError = state
    }



    override suspend fun insertGameCard(gameCardItem: GameCardItem) {
       gameCards.add(gameCardItem)
        collectGameCards.postValue(gameCards)
    }

    override suspend fun deleteGameCard(gameCardItem: GameCardItem) {
        gameCards.remove(gameCardItem)
        collectGameCards.postValue(gameCards)
    }

    override fun getAllGameCards(): Flow<List<GameCardItem>> {
        return collectGameCards.asFlow()
    }

    override suspend fun searchImage(search: String): BaseResult<ImageResponse> {
      return if (networkRequestReturnError){
          BaseResult.error("image service Not Available")
      }else BaseResult.success(ImageResponse(listOf() ,0 , 0))
    }
}