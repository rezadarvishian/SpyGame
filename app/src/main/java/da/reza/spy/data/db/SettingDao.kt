package da.reza.spy.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.model.SettingEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SettingDao {


    @Insert
    suspend fun setSetting(item:SettingEntity)

    @Query("SELECT isMusicEnable FROM SettingEntity  WHERE id = 1")
    fun isMusicEnable(): Flow<Boolean>

    @Query("SELECT isSoundEnable FROM SettingEntity WHERE id = 1 ")
    fun isSoundEnable(): Flow<Boolean>

    @Query("UPDATE SettingEntity SET isMusicEnable =  NOT isMusicEnable WHERE id = 1")
    suspend fun changeMusicSetting()

    @Query("UPDATE SettingEntity SET isSoundEnable = NOT isSoundEnable WHERE id = 1")
    suspend fun changeSoundSetting()

}