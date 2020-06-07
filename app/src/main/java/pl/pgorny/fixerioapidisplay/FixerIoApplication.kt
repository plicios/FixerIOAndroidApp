package pl.pgorny.fixerioapidisplay

import android.app.Application
import timber.log.Timber

class FixerIoApplication : Application() {

    //TODO on rotate loader is not shown
    //change to use api instead of mock

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}