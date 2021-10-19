package da.reza.spy.ui

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.perf.metrics.AddTrace
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import da.reza.spy.BuildConfig
import da.reza.spy.R
import da.reza.spy.Spy
import da.reza.spy.data.model.SettingEntity
import da.reza.spy.utiles.observeInLifecycle
import da.reza.spy.viewModel.GameViewModel
import da.reza.spy.viewModel.WordViewModel
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ir.tapsell.plus.model.AdNetworkError

import ir.tapsell.plus.model.AdNetworks

import ir.tapsell.plus.TapsellPlusInitListener

import ir.tapsell.plus.TapsellPlus




class MainActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModel()
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val TAG = "TAG"
    }

    @AddTrace(name = "mainActivityOnCreate", enabled = true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeViewModel()
        initTapsellPlus()
        initFireBaseMessaging()
    }

    private fun initFireBaseMessaging() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("notification", "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result!!
            Log.w("notification", "token:$token")
        })

    }

    private fun observeViewModel() {


        viewModel.isSoundEnable.onEach {
            Spy.isSoundEnable = it
        }.observeInLifecycle(this)


    }

    private fun initTapsellPlus(){
        TapsellPlus.initialize(this, BuildConfig.TAPSELL_KEY,
            object : TapsellPlusInitListener {
                override fun onInitializeSuccess(adNetworks: AdNetworks) {
                    Log.d("onInitializeSuccess", adNetworks.name)
                }
                override fun onInitializeFailed(
                    adNetworks: AdNetworks,
                    adNetworkError: AdNetworkError
                ) {
                    Log.e(
                        "onInitializeFailed",
                        "ad network: " + adNetworks.name + ", error: " + adNetworkError.errorMessage
                    )
                    Toast.makeText(this@MainActivity, getString(R.string.tapsellError), Toast.LENGTH_SHORT).show()
                    val firebaseAnalytics = Firebase.analytics
                    firebaseAnalytics.logEvent("tapsellStarterError"){
                        param("adNetworkName",adNetworks.name)
                        param("error", adNetworkError.errorMessage)

                    }

                }
            })
        TapsellPlus.setGDPRConsent(this, true)
    }

    override fun onResume() {
        viewModel.isMusicEnable.onEach {
            if (it) {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(this, R.raw.sound_track)
                mediaPlayer!!.isLooping = true
                mediaPlayer!!.start()
            } else {
                mediaPlayer?.pause()
            }
        }.observeInLifecycle(this)
        super.onResume()
    }

    override fun onPause() {
        mediaPlayer?.pause()
        super.onPause()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }

}