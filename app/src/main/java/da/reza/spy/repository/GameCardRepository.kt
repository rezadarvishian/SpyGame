package da.reza.spy.repository

import da.reza.spy.data.db.GameCardDao
import da.reza.spy.data.db.PlayerDao
import da.reza.spy.data.db.SettingDao
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.model.PlayerNames
import da.reza.spy.data.model.SettingEntity
import da.reza.spy.data.remote.ImageApi
import da.reza.spy.data.remote.responses.ImageResponse
import da.reza.spy.data.remote.responses.VazheYabResponse
import da.reza.spy.utiles.AppPreferences
import da.reza.spy.utiles.BaseDataSource
import da.reza.spy.utiles.BaseResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class GameCardRepository(
    private val dao: GameCardDao,
    private val settingDao: SettingDao,
    private val playerDao: PlayerDao,
) : BaseDataSource() {


    suspend fun getWordListSize() = dao.getWordListSize()
    suspend fun changeMusicSetting() = settingDao.changeMusicSetting()
    suspend fun changeSoundSetting() = settingDao.changeSoundSetting()
    suspend fun addPlayer(player: PlayerNames) = playerDao.insertPlayer(player)
    suspend fun addWinCounterForSpyPlayer(name:String) = playerDao.updatePLayerWinCounter(name)
    suspend fun addPlayCounterForPlayer(name:String) = playerDao.updatePLayerPlayCounter(name)
    fun isMusicEnable() = settingDao.isMusicEnable()
    fun isSoundEnable() = settingDao.isSoundEnable()
    fun allPlayerNames() = playerDao.getPLayersList()

    suspend fun getRandomGameCard(): GameCardItem {
        val card = dao.getRandomWord()
        if (card.autoDelete) dao.deleteGameCard(card)
        return card
    }

    fun isFirstRun(): Boolean {
        return AppPreferences.isFirstRun
    }

    fun setFirstRun(Bool: Boolean) {
        AppPreferences.isFirstRun = Bool
    }



}