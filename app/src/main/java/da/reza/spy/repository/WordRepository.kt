package da.reza.spy.repository

import da.reza.spy.data.db.GameCardDao
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.remote.ImageApi
import da.reza.spy.data.remote.responses.ImageResponse
import da.reza.spy.data.remote.responses.VazheYabResponse
import da.reza.spy.utiles.AppPreferences
import da.reza.spy.utiles.BaseDataSource
import da.reza.spy.utiles.BaseResult
import kotlinx.coroutines.flow.Flow

class WordRepository(
    private val api:ImageApi,
    private val cardDao: GameCardDao,
) :BaseDataSource(){

    fun getAllGameCards() = cardDao.getAllGameCard()

    suspend fun deleteGameCard(gameCardItem: GameCardItem)=cardDao.deleteGameCard(gameCardItem)

    suspend fun insertGameCard(gameCardItem: GameCardItem)= cardDao.insertGameCard(gameCardItem)

    suspend fun changeGameCardAutoDelete(GameCard: GameCardItem) =cardDao.changeGameCardAutoDelete(gameCardID = GameCard.id!! , state = GameCard.autoDelete )

    suspend fun deleteAllWords() = cardDao.deleteAll()

    suspend fun searchImage(search: String)=getResult { api.searchForImage(searchString = search) }

    suspend fun suggestWord(word: String) = getResult { api.suggestWord(word = word) }
    suspend fun wordMeaning(word: String) = getResult { api.wordMeaning(word = word) }
    suspend fun translateWord(word: String) = getResult { api.translateWord(word = word) }






}