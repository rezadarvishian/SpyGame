package da.reza.spy.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import da.reza.spy.R
import da.reza.spy.Spy


class CustomImageView(context: Context, attributionSource: AttributeSet):
    AppCompatImageView(context,attributionSource) {

    private var mediaPlayer: MediaPlayer? = null


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
       mediaPlayer = MediaPlayer.create(context, R.raw.click_effect_)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (Spy.isSoundEnable) mediaPlayer?.start()

        return super.onTouchEvent(event)
    }


    override fun onDetachedFromWindow() {
        mediaPlayer?.release()
        super.onDetachedFromWindow()
    }

}
