package ubuntudroid.chimney

import android.app.Application
import org.koin.android.ext.android.startKoin
import ubuntudroid.chimney.di.steamNetworkModule

class ChimneyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(steamNetworkModule))
    }
}