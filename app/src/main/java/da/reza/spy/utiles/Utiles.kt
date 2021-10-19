package da.reza.spy.utiles

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import da.reza.spy.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import da.reza.spy.utiles.ConstVal.PIXABAY_BASE_URL


@SuppressLint("DiscouragedPrivateApi")
fun Context.replaceFont(font: String) {
    val newMap = HashMap<String, Typeface>()
    newMap["sans-serif"] = Typeface.createFromAsset(this.assets, font)
    newMap["sans-serif-smallcaps"] = Typeface.createFromAsset(this.assets, font)
    newMap["sans-serif-medium"] = Typeface.createFromAsset(this.assets, font)
    newMap["monospace"] = Typeface.createFromAsset(this.assets, font)
    newMap["sans-serif-light"] = Typeface.createFromAsset(this.assets, font)
    newMap["sans-serif-black"] = Typeface.createFromAsset(this.assets, font)
    newMap["serif-monospace"] = Typeface.createFromAsset(this.assets, font)
    newMap["sans-serif-condensed"] = Typeface.createFromAsset(this.assets, font)
    newMap["sans-serif-condensed-light"] = Typeface.createFromAsset(this.assets, font)
    newMap["sans-serif-condensed-medium"] = Typeface.createFromAsset(this.assets, font)
    newMap["sans-serif-thin"] = Typeface.createFromAsset(this.assets, font)
    try {
        val staticField = Typeface::class.java.getDeclaredField("sSystemFontMap")
        staticField.isAccessible = true
        staticField.set(null, newMap)
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }
}


@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("loadImage")
fun ImageView.loadImage(Picture: String?) {

    if (!Picture.isNullOrBlank()){
        if (Picture.contains("https").or(Picture.contains(PIXABAY_BASE_URL))){
            val options= RequestOptions().error(R.drawable.detective)
            Glide.with(this.context)
                .setDefaultRequestOptions(options)
                .load(Picture)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .into(this)
        }else {
             Picture.convertBase64ToBitmap()?.let {
                this.setImageBitmap(it)
            }

        }
    }else {
        this.setImageDrawable(AppCompatResources.getDrawable(this.context,R.drawable.detective))
    }

}

fun Bitmap?.convertBitmapToBase64(): String {
    return if (this != null){
        var compress = 50
        val size = this.byteCount
        //if size more than 1MB use more compress
        if (size.toFloat() / 10000000 > 1.0) compress = 40
        if (size.toFloat() / 10000000 > 2.0) compress = 30
        if (size.toFloat() / 10000000 > 4.0) compress = 10
        val bos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, compress, bos)
        val bytesArray = bos.toByteArray()
        return Base64.encodeToString(bytesArray, Base64.DEFAULT)
    }else ""
}


fun String.convertBase64ToBitmap():Bitmap? {
    val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return if (decodedString.isNotEmpty() && decodedString != null){
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
         decodedByte
    }else null

}

fun View.setGone() {this.visibility = View.GONE}

fun View.setVisible() {this.visibility = View.VISIBLE}

fun View.setInvisible() {this.visibility = View.INVISIBLE}

fun Boolean.asLongValue() = if (this) 0L else 1L

fun Activity.hideKeyboard() {
    Looper.myLooper()?.let {
        Handler(it).postDelayed({
            val view = this.findViewById<View>(android.R.id.content)
            if (view != null) {
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }, 300)
    }

}

fun showKeyboard(activity: Activity) {
    val inputMethodManager =
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Activity.showToast(message:String?){
    message?.let {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

fun View.animateInvisible(root: ViewGroup){
    val transition : Transition = Slide(Gravity.BOTTOM)
    transition.addTarget(this).duration = 1000
    TransitionManager.beginDelayedTransition(root, transition)
    this.setGone()
}

fun View.animateVisible(root: ViewGroup){
    val transition : Transition = Slide(Gravity.BOTTOM)
    transition.addTarget(this).duration = 1000
    TransitionManager.beginDelayedTransition(root, transition)
    this.setVisible()
}