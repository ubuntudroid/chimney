package ubuntudroid.chimney.data.steam.user

import android.arch.lifecycle.LiveData
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

    private val playerSummaryLiveDatas: MutableMap<String, LiveData<Resource<PlayerModel>>> = mutableMapOf()

    fun getPlayerSummaries(steamId: String): LiveData<Resource<PlayerModel>> {
        // return if we already have a live data for this id request
        playerSummaryLiveDatas[steamId]?.apply { return this }

        val networkLiveData = PlayerResource(steamId, steamUserDao, steamUserApi, networkService).getLiveData()

        // TODO remove from in-memory cache as soon as we have no more observers

        playerSummaryLiveDatas[steamId] = networkLiveData

        return networkLiveData
    }
}

