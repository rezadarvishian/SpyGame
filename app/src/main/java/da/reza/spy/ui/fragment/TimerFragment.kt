package da.reza.spy.ui.fragment

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import da.reza.spy.R
import da.reza.spy.databinding.TimerBinding
import da.reza.spy.ui.base.BaseFragment
import da.reza.spy.ui.dialog.MaterialQuestionDialog
import da.reza.spy.viewModel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit
import android.app.AlarmManager

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context

import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import da.reza.spy.BuildConfig
import da.reza.spy.databinding.FinishGameBinding
import da.reza.spy.ui.dialog.FinishGameDialog
import da.reza.spy.utiles.*
import ir.tapsell.plus.AdHolder
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
import org.koin.core.component.KoinApiExtension


class TimerFragment : BaseFragment<TimerBinding>(R.layout.fragment_timer) {

    private val viewModel:GameViewModel by activityViewModels()
    override fun adContainerID() =binding.adContainer
    private var timer:CountDownTimer? = null
    private var mediaPlayer : MediaPlayer? = null
    private var gameDuration = 0
    override fun onBackPress() {
        MaterialQuestionDialog(requireActivity())
            .setTitle(getString(R.string.exit_title2))
            .setCancelTitle(getString(R.string.exit_dialog_cancel_button))
            .setConfirmTitle(getString(R.string.exit_dialog_confirm_button))
            .setAnimationResId(R.raw.close)
            .setOnCancel {}
            .setOnConfirm {
                findNavController().navigate(R.id.TimerToHome)
                cancelAlarm()
            }
            .show()
    }


    @KoinApiExtension
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(),R.raw.lose_effect)
        setUpCounter()

        binding.btnSpyFound.setOnClickListener {
            finishedGameDialog(true)
        }

        binding.stopGame.setOnClickListener {
            onBackPress()
        }

        binding.btnSpyNotFound.setOnClickListener {
            finishedGameDialog(false)
        }

        binding.iconMusic.setOnClickListener {
            viewModel.changeMusicSetting()
        }

    }

    @KoinApiExtension
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun setUpCounter(){
        viewModel.timerAmount.observe(viewLifecycleOwner){
            it.let {
                setAlarm(System.currentTimeMillis()+it*(60000))
                timer = object: CountDownTimer(it*(60000).toLong(), 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                        val min = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))
                        val second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))

                        binding.timer.text = String.format("%02d:%02d",min,second)
                        gameDuration += 1000

                        when (millisUntilFinished){

                            in 60000 .. 120000 -> {
                                binding.timer.setTextColor(resources.getColor(R.color.yellow))
                            }
                            in 0 .. 60000 -> {
                                binding.timer.setTextColor(resources.getColor(R.color.red))
                            }
                        }


                    }

                    override fun onFinish() {
                        binding.timer.text = getString(R.string.spyNotFound)
                        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                            delay(1000)
                            mediaPlayer?.start()
                            delay(2000)
                            finishedGameDialog(false)
                        }
                    }
                }
                timer!!.start()


            }

        }

        viewModel.isMusicEnable.onEach {
            if (it){
                binding.iconMusic.setImageDrawable(requireActivity().resources.getDrawable(R.drawable.ic_round_music_note_24,null))
            }else {
                binding.iconMusic.setImageDrawable(requireActivity().resources.getDrawable(R.drawable.ic_round_music_off_24,null))
            }
        }.observeInLifecycle(this)

        viewModel.gameCardForPlay.observe(viewLifecycleOwner){
            it.forEach {
                if (it.isStarterPlayer && it.playerName.isNotBlank()){
                    binding.txtStarterName.text = "بازی با ${it.playerName} شروع میشه!"
                }
            }
        }

    }

    @KoinApiExtension
    private fun finishedGameDialog(spyFound:Boolean){
        cancelAlarm()
        FinishGameDialog()
            .setSpyFound(spyFound)
            .show(requireActivity().supportFragmentManager , "FinishGame")
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarm(time:Long){
        val am = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, TimerAlarm::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, FLAG_UPDATE_CURRENT)
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time,pi)

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun cancelAlarm(){
        val am = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, TimerAlarm::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, FLAG_UPDATE_CURRENT)
        am.cancel(pi)
        timer?.cancel()
        requireActivity().stopService(Intent(requireContext(),PlayMediaService::class.java))

    }

    override fun onDestroyView() {
        timer?.cancel()
        mediaPlayer?.release()
        super.onDestroyView()
    }

    override fun onDestroy() {
        cancelAlarm()
        super.onDestroy()
    }
}