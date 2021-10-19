package da.reza.spy.ui.dialog

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import da.reza.spy.R
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.utiles.loadImage
import da.reza.spy.utiles.setGone
import da.reza.spy.utiles.setVisible

class ShowImageDialog(activity:Activity ,val listener: DialogListener):BottomSheetDialog(activity, R.style.NewDialog) {


    private var gameCard : GameCardItem? = null


    fun setGameCard(item:GameCardItem) = apply {
        gameCard = item
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_uamge_dialog)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        setCancelable(false)

        val btnClose = findViewById<AppCompatButton>(R.id.btnClose)!!
        val imageView =findViewById<AppCompatImageView>(R.id.imageView)!!
        val textView = findViewById<AppCompatTextView>(R.id.txtWord)!!
        val txtStarter = findViewById<AppCompatTextView>(R.id.txtStarter)!!


        gameCard?.let {


            if (gameCard!!.cardImage.isNotBlank()){
                imageView.loadImage(gameCard!!.cardImage)
                imageView.setVisible()
            }else {
                imageView.setGone()
            }

            textView.text = gameCard!!.cardName

            if (gameCard!!.isStarterPlayer) txtStarter.setVisible()


        }


        btnClose.setOnClickListener {
            dismiss()
            listener.onCloseClicked()
        }


    }


    interface DialogListener{
        fun onCloseClicked()
    }

}

