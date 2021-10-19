package da.reza.spy.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import da.reza.spy.R
import da.reza.spy.Spy
import da.reza.spy.repository.GameCardRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CustomButton(context:Context , attributionSource: AttributeSet):AppCompatButton(context,attributionSource) , KoinComponent {



    private var mediaPlayer:MediaPlayer? = null


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
