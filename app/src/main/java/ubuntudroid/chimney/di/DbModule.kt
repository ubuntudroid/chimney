package ubuntudroid.chimney.di

import android.arch.persistence.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext
import ubuntudroid.chimney.db.steam.SteamDatabase

val dbModule = applicationContext {

    // SteamDatabase
    bean {
        Room.databaseBuilder(androidApplication(), SteamDatabase::class.java, "steam-db").build()
    }

    //SteamUserDao
    bean {
        (get() as SteamDatabase).steamUserDao()
    }
}