package ubuntudroid.chimney

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.with
import org.koin.standalone.StandAloneContext
import timber.log.Timber
import ubuntudroid.chimney.di.ApplicationComponent

class ChimneyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        StandAloneContext.startKoin(ApplicationComponent.modules) with this
    }
}