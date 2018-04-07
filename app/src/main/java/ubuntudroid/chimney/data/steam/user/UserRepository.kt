package ubuntudroid.chimney.data.steam.user

import android.arch.lifecycle.LiveData
import ubuntudroid.chimney.data.Refreshable
import ubuntudroid.chimney.data.Resource
import ubuntudroid.chimney.data.steam.user.db.SteamUserDao
import ubuntudroid.chimney.data.steam.user.resources.PlayerResource
import ubuntudroid.chimney.data.network.NetworkService
import ubuntudroid.chimney.data.steam.user.network.SteamUserApi


class UserRepository(
        private val steamUserApi: SteamUserApi,
        private val steamUserDao: SteamUserDao,
        private val networkService: NetworkService
) {

    fun getPlayer(steamId: String): LiveData<Resource<PlayerModel>> =
            PlayerResource(steamId, steamUserDao, steamUserApi, networkService)

    fun refreshPlayer(liveData: LiveData<Resource<PlayerModel>>) {
        if (liveData is Refreshable) {
            liveData.refresh()
        } else {
            throw IllegalArgumentException("LiveData isn't refreshable!")
        }
    }
}

