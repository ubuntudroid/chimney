package ubuntudroid.chimney.network.steam.user

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ubuntudroid.chimney.network.steam.SteamResponseModel

private const val INTERFACE = "ISteamUser"

interface SteamUserApi {

    @GET("${INTERFACE}/GetPlayerSummaries/v2")
    fun getPlayerSummaries(@Query("steamids") steamIds: List<String>)
            : Deferred<SteamResponseModel<GetPlayerSummariesResponse>>
}
