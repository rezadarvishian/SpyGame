package da.reza.spy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class SettingEntity(
    @PrimaryKey(autoGenerate = true)
     var id :Int? = null,
    var isMusicEnable:Boolean = true,
    var isSoundEnable:Boolean = true
)