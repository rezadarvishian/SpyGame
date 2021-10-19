package da.reza.spy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.model.PlayerNames
import da.reza.spy.data.model.SettingEntity


@Database(
    entities =[ GameCardItem::class , SettingEntity::class , PlayerNames::class],
    version = 1
)
abstract class GameCardDataBase : RoomDatabase() {

    abstract fun GameCardDao () :GameCardDao
    abstract fun settingDao () :SettingDao
    abstract fun PlayerDao () :PlayerDao

}