package da.reza.spy.repository

import da.reza.spy.data.db.GameCardDao
import da.reza.spy.data.db.SettingDao
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.model.SettingEntity
import da.reza.spy.utiles.AppPreferences

class SplashScreenRepository(
    private val settingDao: SettingDao,
    private val cardDao: GameCardDao
    ) {

    fun isFirstRun(): Boolean {
        return AppPreferences.isFirstRun
    }

    fun setFirstRun(Bool: Boolean) {
        AppPreferences.isFirstRun = Bool
    }

    suspend fun insertGameCard(gameCardItem: GameCardItem) {
        cardDao.insertGameCard(gameCardItem)
    }

    suspend fun setSetting(item: SettingEntity) {
        settingDao.setSetting(item)
    }

}