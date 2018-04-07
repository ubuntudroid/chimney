package ubuntudroid.chimney.data.steam.user.resources

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import retrofit2.HttpException
import timber.log.Timber
import ubuntudroid.chimney.data.network.NetworkBoundResource
import ubuntudroid.chimney.data.Resource
import ubuntudroid.chimney.data.steam.user.PlayerModel
import ubuntudroid.chimney.data.steam.user.db.SteamUserDao
import ubuntudroid.chimney.data.network.NetworkService
import ubuntudroid.chimney.data.steam.user.network.SteamUserApi
import ubuntudroid.chimney.util.livedata.CoroutineLiveData
import java.io.IOException

class PlayerResource(
        private val steamId: String,
        private val steamUserDao: SteamUserDao,
        private val steamUserApi: SteamUserApi,
        private val networkService: NetworkService
) : NetworkBoundResource<PlayerModel, PlayerModel>() {

    override fun saveCallResult(item: PlayerModel) {
        steamUserDao.upsert(item.copy(timestamp = System.currentTimeMillis()))
    }

    override fun shouldFetch(data: PlayerModel?): Boolean =
            networkService.isConnectedNow()
                    && (data == null
                        || System.currentTimeMillis() - data.timestamp > 1000 * 60 * 60)

    override fun loadFromDb(): LiveData<PlayerModel> = steamUserDao.getById(steamId)

    override fun createCall(): LiveData<Resource<PlayerModel>> {
        return object : CoroutineLiveData<Resource<PlayerModel>>() {

            override suspend fun runInBackground(): Resource<PlayerModel> = try {
                val playerSummaries = steamUserApi.getPlayerSummaries(listOf(steamId)).await()
                val player = playerSummaries.response.players.first { steamId == it.steamId }
                Resource.success(player)
            } catch (e: HttpException) {
                // non 200 response
                Timber.e(e)
                Resource.error("Request failed: ${e.code()} ${e.message}")
            } catch (e: IOException) {
                // network error
                Timber.e(e)
                Resource.error("Network error: ${e.message}")
            } catch (e: NoSuchElementException) {
                // user not found
                Timber.e(e)
                Resource.error("User not found!")
            } catch (e: Exception) {
                Timber.e(e)
                Resource.error("Unexpected error: ${e.message}")
            }
        }
    }
}