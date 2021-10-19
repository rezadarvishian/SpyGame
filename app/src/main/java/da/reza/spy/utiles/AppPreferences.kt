package da.reza.spy.utiles

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow

object AppPreferences {

    private lateinit var preferences: SharedPreferences
    private val FIRSTRUN = Pair("isFirstRun", true)

    fun init(context: Context) {
        preferences = context.getSharedPreferences("NAME", Context.MODE_PRIVATE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }


    var isFirstRun: Boolean
        get() = preferences.getBoolean(FIRSTRUN.first, FIRSTRUN.second)
        set(value) = preferences.edit {
            it.putBoolean(FIRSTRUN.first, value)
        }




}


