package ubuntudroid.chimney.data.user

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import retrofit2.HttpException
import timber.log.Timber
import ubuntudroid.chimney.data.State
import ubuntudroid.chimney.network.steam.user.PlayerModel
import ubuntudroid.chimney.network.steam.user.SteamUserApi
import java.io.IOException


class UserRepository(private val steamUserApi: SteamUserApi) {

    private val playerSummaryLiveDatas: MutableMap<String, LiveData<State<PlayerModel>>> = mutableMapOf()

    fun getPlayerSummaries(steamId: String): LiveData<State<PlayerModel>> {
        // return if we already have a live data for this id request
        playerSummaryLiveDatas[steamId]?.apply { return this }

        // TODO we definitely want to try and condense this code -
        // a first step could be to move this to a proper LiveData sub-class,
        // then refactor the redundant parts into an abstract parent class. If properly
        // done we then might even be able to get rid of the sub-class and inline again if there
        // is just a few lines of code left
        val liveData = object : LiveData<State<PlayerModel>>() {

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
                        postValue(State(player))
                    } catch (e: HttpException) {
                        // non 200 response
                        Timber.e(e)
                        postValue(State(value = null, error = "Request failed: ${e.code()} ${e.message}"))
                    } catch (e: IOException) {
                        // network error
                        Timber.e(e)
                        postValue(State(value = null, error = "Network error: ${e.message}"))
                    } catch (e: NoSuchElementException) {
                        // user not found
                        Timber.e(e)
                        postValue(State(value = null, error = "User not found!"))
                    } catch (e: Exception) {
                        Timber.e(e)
                        postValue(State(null, e.message))
                    }
                }
            }

            override fun onInactive() {
                deferred?.cancel()
                super.onInactive()
            }
        }
        playerSummaryLiveDatas[steamId] = liveData

        return liveData
    }
}