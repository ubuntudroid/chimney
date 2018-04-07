package ubuntudroid.chimney.data.steam.user.network

import com.google.gson.annotations.SerializedName
import ubuntudroid.chimney.data.steam.user.PlayerModel

data class GetPlayerSummariesResponse(
        @SerializedName("players") val players: List<PlayerModel>
)