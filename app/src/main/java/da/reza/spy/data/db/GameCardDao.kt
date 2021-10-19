package da.reza.spy.data.db

import androidx.room.*
import da.reza.spy.data.model.GameCardItem
import kotlinx.coroutines.flow.Flow

@Dao
interface GameCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameCardItem: GameCardItem)

    @Delete
    suspend fun deleteGameCard(gameCardItem: GameCardItem)

    @Query("SELECT * FROM GameCardItem")
    fun getAllGameCard():Flow<List<GameCardItem>>

    @Query("SELECT * FROM GameCardItem ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord():GameCardItem

    @Query("UPDATE GameCardItem SET autoDelete = :state WHERE id = :gameCardID")
    suspend fun changeGameCardAutoDelete(gameCardID:Int , state:Boolean)

    @Query("DELETE FROM GameCardItem")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM GameCardItem")
    suspend fun getWordListSize() :Int

    @Query("SELECT COUNT(*) FROM GameCardItem WHERE cardName==:str")
    suspend fun isExist(str:String) :Int

    @Query("DELETE FROM GameCardItem WHERE cardName==:str")
    suspend fun delete(str:String) :Int


    @Transaction
    suspend fun insertGameCard(gameCardItem: GameCardItem){
        if (isExist(gameCardItem.cardName)>0){
            delete(gameCardItem.cardName)
            insert(gameCardItem)
        }else {
            insert(gameCardItem)
        }
    }

}