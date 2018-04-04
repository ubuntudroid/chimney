package ubuntudroid.chimney.data.user

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import retrofit2.HttpException
import ubuntudroid.chimney.data.State
import ubuntudroid.chimney.network.steam.user.PlayerModel
import ubuntudroid.chimney.network.steam.user.SteamUserApi
import java.io.IOException


class UserRepository(private val steamUserApi: SteamUserApi) {

    private val playerSummaryLiveDatas: MutableMap<String, LiveData<State<PlayerModel>>> = mutableMapOf()

    fun getPlayerSummaries(steamId: String): LiveData<State<PlayerModel>> {
        // return if we already have a live data for this id request
        playerSummaryLiveDatas[steamId]?.apply { return this }

        val liveData = object : LiveData<State<PlayerModel>>() {

            var deferred: Deferred<Unit>? = null

            override fun onActive() {
                super.onActive()

                if (deferred?.isCancelled == false) {
                    return
                }

                deferred = async {
                    value = try {
                        val playerSummaries = steamUserApi.getPlayerSummaries(listOf(steamId)).await()
                        val player = playerSummaries.response.players.first { steamId == it.steamId }
                        State(player)
                    } catch (e: HttpException) {
                        // non 200 response
                        State(value = null, error = "Request failed: ${e.code()} ${e.message}")
                    } catch (e: IOException) {
                        // network error
                        State(value = null, error = "Network error: ${e.message}")
                    } catch (e: NoSuchElementException) {
                        // user not found
                        State(value = null, error = "User not found!")
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