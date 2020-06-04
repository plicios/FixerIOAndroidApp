package pl.pgorny.fixerioapidisplay

import android.app.Application
import timber.log.Timber

class FixerIoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        const val apiBaseUrl = "https://wger.de/api/v2/"
    }
}