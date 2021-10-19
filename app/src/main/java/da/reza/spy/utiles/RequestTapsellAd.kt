package da.reza.spy.utiles

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import da.reza.spy.BuildConfig
import da.reza.spy.R
import ir.tapsell.plus.AdHolder
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel

class RequestTapsellAd {


    private lateinit var activity:Activity
    private var adContainer:ViewGroup? = null
    private var adHolder :AdHolder? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var screenName = ""
    private var nativeAdResponseId = ""


    fun setActivity(ac:Activity) = apply {
        this.activity = ac
    }

    fun setAdViewGroup(vg:ViewGroup?) = apply {
        this.adContainer=vg
    }

    fun setScreenName(name:String) = apply {
        this.screenName = name
    }

    fun build():RequestTapsellAd{
        if (adContainer != null){
            adHolder = TapsellPlus.createAdHolder(activity, adContainer, R.layout.native_ad_banner)
            requestAd()
        }
        firebaseAnalytics = Firebase.analytics
        initFireBaseLog()
        return this
    }


    fun destroyNativeBanner(){
        TapsellPlus.destroyNativeBanner(activity, nativeAdResponseId)
    }

    private fun initFireBaseLog(){
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }

    private fun requestAd(){
        TapsellPlus.requestNativeAd(
           activity,
            BuildConfig.NATIVE_AD_ZONE_ID,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    nativeAdResponseId = tapsellPlusAdModel.responseId
                    showAd()
                    Log.d("onInitializeSuccess","ad request is success")

                }

                override fun error(@NonNull message: String?) {
                    Log.d("onInitializeSuccess", "onRequestError : ${message.toString()}")
                    firebaseAnalytics.logEvent("tapsellRequestAdError"){
                        param("screen",screenName)
                        param("error", message.toString())
                    }
                }
            })
    }

    private fun showAd(){
        TapsellPlus.showNativeAd(activity, nativeAdResponseId, adHolder,
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                    Log.d("onInitializeSuccess","onOpened")
                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    Log.d("onInitializeSuccess",tapsellPlusErrorModel.toString())
                    firebaseAnalytics.logEvent("tapsellRequestAdError"){
                        param("screen",screenName)
                        param("error", tapsellPlusErrorModel.errorMessage)
                    }
                    super.onError(tapsellPlusErrorModel)
                }
            })
    }




}