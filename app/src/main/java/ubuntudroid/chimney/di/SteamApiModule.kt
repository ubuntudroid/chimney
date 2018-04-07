package ubuntudroid.chimney.di

import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import ubuntudroid.chimney.data.steam.user.network.SteamUserApi

val steamApiModule = applicationContext {

    // SteamUserApi
    bean {
        (get(STEAM_SCOPE) as Retrofit).create(SteamUserApi::class.java)
    }
}