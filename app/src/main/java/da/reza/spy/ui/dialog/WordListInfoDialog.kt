package da.reza.spy.ui.dialog

import android.app.Activity
import android.os.Bundle
import android.provider.Settings.Global.getString
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import da.reza.spy.R
import da.reza.spy.data.model.GameCardItem
import da.reza.spy.utiles.loadImage


class WordListInfoDialog(val activity: Activity, val listener: DialogListener):
    BottomSheetDialog(activity, R.style.NewDialog) {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_list_info_dialog)
        setCancelable(true)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val btnDelete = findViewById<AppCompatButton>(R.id.btnDelete)!!

        btnDelete.setOnClickListener {
           listener.onDeleteClicked()
            dismiss()
        }

    }


    interface DialogListener{
        fun onDeleteClicked()
    }

}
