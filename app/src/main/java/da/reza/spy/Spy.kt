package da.reza.spy

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import da.reza.spy.di.apiModule
import da.reza.spy.di.dataBaseModule
import da.reza.spy.di.repositoryModule
import da.reza.spy.di.viewModelModule
import da.reza.spy.utiles.AppPreferences
import da.reza.spy.utiles.replaceFont
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Spy:Application() {

    companion object {
        var isSoundEnable = true

    }

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        replaceFont("lalezar.ttf")
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Spy)
            modules(
                listOf(
                    apiModule,
                    dataBaseModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}