package ubuntudroid.chimney.data.steam.user.network

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

private const val INTERFACE = "ISteamUser"

interface SteamUserApi {

    @GET("${INTERFACE}/GetPlayerSummaries/v2")
    fun getPlayerSummaries(@Query("steamids") steamIds: List<String>)
            : Deferred<SteamResponseModel<GetPlayerSummariesResponse>>
}
