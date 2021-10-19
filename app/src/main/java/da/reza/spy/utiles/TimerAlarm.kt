package da.reza.spy.utiles

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import org.koin.core.logger.KOIN_TAG


class TimerAlarm : BroadcastReceiver() {
    @SuppressLint("InvalidWakeLockTag", "WakelockTimeout")
    override fun onReceive(p0: Context?, p1: Intent?) {


        val pm = p0?.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl  = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "timerAlarm")
        wl.acquire()
        Log.i("alaram", "djsakdjaskljd" )
        val intent = Intent(p0,PlayMediaService::class.java)
        p0.startService(intent)

        wl.release()

    }



}