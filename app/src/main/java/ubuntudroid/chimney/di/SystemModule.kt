package ubuntudroid.chimney.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext
import ubuntudroid.chimney.data.network.NetworkService

val systemModule = applicationContext {

    // NetworkService
    bean {
        NetworkService(androidApplication())
    }
}