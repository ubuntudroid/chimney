package ubuntudroid.chimney.network.steam.user

import com.google.gson.annotations.SerializedName

data class GetPlayerSummariesResponse(
        @SerializedName("players") val players: List<PlayerModel>
)