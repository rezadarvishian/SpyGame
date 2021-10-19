package da.reza.spy.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class GameCardItem(
    @PrimaryKey(autoGenerate = true)
    var id :Int? = null ,
    var cardName :String ,
    var cardImage:String ,
    var autoDelete:Boolean=false,
    var isEnable:Boolean = true,
    @Ignore
    var playerName:String = "",
    @Ignore
    var isSpy:Boolean = false,
    @Ignore
    var isStarterPlayer:Boolean = false
){

    constructor():this(id = null, cardName = "", cardImage="", autoDelete = false, isEnable = true)
}

