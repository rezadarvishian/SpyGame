package da.reza.spy.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity
data class PlayerNames(
    @PrimaryKey(autoGenerate = true)
    var id :Int = 0,
    var name:String,
    var WinCounter :Int = 0,
    var spyCounter :Int = 0,
    @Ignore
    var winPercent:Int = 0
){

    constructor():this(id = 0,name = "" , WinCounter = 0 , spyCounter = 0)

}
