package da.reza.spy.utiles

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import da.reza.spy.R
import da.reza.spy.ui.MainActivity
import org.koin.android.ext.android.inject

class PlayMediaService : Service() {


    private var mediaPlayer:MediaPlayer? = null

    override fun onBind(p0: Intent?): IBinder? {
       return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("alaram", "service start" )
        mediaPlayer = MediaPlayer.create(this, R.raw.lose_effect)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer?.start()
        sendNotification()
        Log.i("alaram", "service command" )
        return super.onStartCommand(intent, flags, startId)

    }

    override fun stopService(name: Intent?): Boolean {
        mediaPlayer?.release()
        return super.stopService(name)
    }

    private fun sendNotification() {

        val intent: Intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "SpyTimer"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("بازی جاسوس تموم شد ...")
            .setContentText("جاسوس رو تونستین پیدا کنین یا نه ؟ ")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        notificationBuilder.setSmallIcon(R.drawable.detective_one)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "timer",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

    }



}