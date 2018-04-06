package ubuntudroid.chimney.data.user

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import retrofit2.HttpException
import timber.log.Timber
import ubuntudroid.chimney.data.NetworkBoundResource
import ubuntudroid.chimney.data.Resource
import ubuntudroid.chimney.data.steam.user.PlayerModel
import ubuntudroid.chimney.db.steam.user.SteamUserDao
import ubuntudroid.chimney.network.NetworkService
import ubuntudroid.chimney.network.steam.SteamUserApi
import java.io.IOException


class UserRepository(
        private val steamUserApi: SteamUserApi,
        private val steamUserDao: SteamUserDao,
        private val networkService: NetworkService
) {

    private val playerSummaryLiveDatas: MutableMap<String, LiveData<Resource<PlayerModel>>> = mutableMapOf()

    fun getPlayerSummaries(steamId: String): LiveData<Resource<PlayerModel>> {
        // return if we already have a live data for this id request
        playerSummaryLiveDatas[steamId]?.apply { return this }

        val networkLiveData = object : NetworkBoundResource<PlayerModel, PlayerModel>() {
            override fun saveCallResult(item: PlayerModel) {
                steamUserDao.upsert(item.copy(timestamp = System.currentTimeMillis()))
            }

            override fun shouldFetch(data: PlayerModel?): Boolean =
                    networkService.isConnectedNow()
                            && (data == null
                                || System.currentTimeMillis() - data.timestamp > 1000 * 60 * 60)

            override fun loadFromDb(): LiveData<PlayerModel> = steamUserDao.getById(steamId)

            override fun createCall(): LiveData<Resource<PlayerModel>> {
                // TODO we definitely want to try and condense this code -
                // a first step could be to move this to a proper LiveData sub-class,
                // then refactor the redundant parts into an abstract parent class. If properly
                // done we then might even be able to get rid of the sub-class and inline again if there
                // is just a few lines of code left
                return object : LiveData<Resource<PlayerModel>>() {

                    var deferred: Deferred<Unit>? = null

                    override fun onActive() {
                        super.onActive()

                        if (deferred?.isCancelled == false) {
                            return
                        }

                        deferred = async {
                            try {
                                val playerSummaries = steamUserApi.getPlayerSummaries(listOf(steamId)).await()
                                val player = playerSummaries.response.players.first { steamId == it.steamId }
                                postValue(Resource.success(player))
                            } catch (e: HttpException) {
                                // non 200 response
                                Timber.e(e)
                                postValue(Resource.error("Request failed: ${e.code()} ${e.message}"))
                            } catch (e: IOException) {
                                // network error
                                Timber.e(e)
                                postValue(Resource.error("Network error: ${e.message}"))
                            } catch (e: NoSuchElementException) {
                                // user not found
                                Timber.e(e)
                                postValue(Resource.error("User not found!"))
                            } catch (e: Exception) {
                                Timber.e(e)
                                postValue(Resource.error("Unexpected error: ${e.message}"))
                            }
                        }
                    }

                    override fun onInactive() {
                        deferred?.cancel()
                        super.onInactive()
                    }
                }
            }
        }.asLiveData

        // TODO remove from in-memory cache as soon as we have no more observers

        playerSummaryLiveDatas[steamId] = networkLiveData

        return networkLiveData
    }
}