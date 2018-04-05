package ubuntudroid.chimney.di

import org.koin.dsl.module.applicationContext
import ubuntudroid.chimney.data.user.AccountManager

val authModule = applicationContext {

    // AccountManager
    bean {
        AccountManager()
    }
}