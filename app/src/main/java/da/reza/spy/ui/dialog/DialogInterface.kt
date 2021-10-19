package da.reza.spy.ui.dialog

import android.graphics.Bitmap
import da.reza.spy.data.model.GameCardItem

abstract interface DialogInterface {

    fun saveData(item: GameCardItem)
    fun imageLoaded(item: Bitmap)

}