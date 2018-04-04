package ubuntudroid.chimney.network.steam

import com.google.gson.annotations.SerializedName

data class SteamResponseModel<out T>(
        @SerializedName("response") val response: T
)