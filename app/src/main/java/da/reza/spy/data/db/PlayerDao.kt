package da.reza.spy.data.db

import androidx.room.*
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.data.model.PlayerNames
import da.reza.spy.data.model.SettingEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayerDao {


    @Insert
    suspend fun insert(item: PlayerNames)

    @Query("SELECT * FROM PlayerNames")
    fun getPLayersList(): Flow<List<PlayerNames>>

    @Query("UPDATE PlayerNames SET  WinCounter = WinCounter+1 WHERE name = :playerName")
    suspend fun updatePLayerWinCounter(playerName: String)

    @Query("UPDATE PlayerNames SET spyCounter = spyCounter+1 WHERE name = :playerName")
    suspend fun updatePLayerPlayCounter(playerName: String)

    @Query("SELECT COUNT(name) FROM PlayerNames WHERE name = :playerName")
    suspend fun userExist(playerName: String): Int

    @Delete
    suspend fun deletePlayer(player: PlayerNames)


    @Transaction
    suspend fun insertPlayer(item: PlayerNames): String {
        return if (userExist(item.name) >= 1) {
            "اسم بازیکن تکراریه و تو لیست بالا هست، اسمش باید خاص باشه"
        } else {
            insert(item)
            " "
        }
    }

}