package ubuntudroid.chimney.di

import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import ubuntudroid.chimney.network.steam.user.SteamUserApi

val steamApiModule = applicationContext {

    // SteamUserApi
    bean {
        (get(STEAM_SCOPE) as Retrofit).create(SteamUserApi::class.java)
    }
}