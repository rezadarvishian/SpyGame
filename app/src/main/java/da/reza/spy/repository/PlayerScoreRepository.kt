package da.reza.spy.repository

import da.reza.spy.data.db.GameCardDao
import da.reza.spy.data.db.PlayerDao
import da.reza.spy.data.db.SettingDao
import da.reza.spy.data.model.PlayerNames
import da.reza.spy.data.remote.ImageApi
import da.reza.spy.utiles.BaseDataSource

class PlayerScoreRepository (private val playerDao: PlayerDao) :BaseDataSource() {



    fun getPlayerScoreList() = playerDao.getPLayersList()

    suspend fun deletePlayer(player:PlayerNames) = playerDao.deletePlayer(player)

}