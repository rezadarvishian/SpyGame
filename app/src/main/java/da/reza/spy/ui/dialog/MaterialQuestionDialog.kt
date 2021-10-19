package da.reza.spy.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.lottie.LottieAnimationView
import da.reza.spy.R
import da.reza.spy.utiles.setGone
import da.reza.spy.utiles.setVisible


class MaterialQuestionDialog(private val activity: Activity, ) : Dialog(activity) {


    private val btnCancel by lazy { findViewById<AppCompatButton>(R.id.btnCancel) }
    private val btnConfirm by lazy { findViewById<AppCompatButton>(R.id.btnConfirm) }
    private val animationView by lazy { findViewById<LottieAnimationView>(R.id.iconAnimation) }
    private val txtTitle by lazy { findViewById<AppCompatTextView>(R.id.txtTitle) }


    private var confirm: View.OnClickListener? = null
    private var confirmTitle = ""
    private var cancel: View.OnClickListener? = null
    private var cancelTitle = ""
    private var isShowCancelButton: Boolean = true
    private var isShowConfirmButton: Boolean = true
    private var title = ""
    private var AnimationId : Int ? = null



    fun setOnCancel(cancel: View.OnClickListener) = apply {
        this.cancel = cancel
    }

    fun setOnConfirm(confirm: View.OnClickListener) = apply {
        this.confirm = confirm
    }

    fun showCancelButton(state:Boolean) = apply {
        this.isShowCancelButton = state
    }

    fun showConfirmButton(state:Boolean) = apply {
        this.isShowConfirmButton = state
    }

    fun setTitle(text: String) = apply {
        this.title = text
    }
    fun setAnimationResId(id: Int) = apply {
        this.AnimationId = id
    }

    fun setCancelTitle(str:String) = apply {
        this.cancelTitle = str
    }

    fun setConfirmTitle(str:String) = apply {
        this.confirmTitle = str
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.question_dialog)
        window!!.attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        //btnConfirmWallet.setOnClickListener { dismiss() }

        txtTitle.text = title

        if (isShowCancelButton) btnCancel.setVisible() else btnCancel.setGone()
        if (isShowConfirmButton) btnConfirm.setVisible() else btnConfirm.setGone()

        btnConfirm.text = confirmTitle
        btnCancel.text = cancelTitle

        AnimationId?.let { animationView.setAnimation(it) }

        confirm?.apply {
            btnConfirm.setOnClickListener {
                this.onClick(it)
                dismiss()
            }
        }

        cancel?.apply {
            btnCancel.setOnClickListener {
                this.onClick(it)
                dismiss()
            }
        }

    }
}



