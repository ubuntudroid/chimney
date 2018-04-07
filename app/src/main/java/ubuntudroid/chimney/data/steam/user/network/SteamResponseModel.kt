package ubuntudroid.chimney.data.steam.user.network

import com.google.gson.annotations.SerializedName

data class SteamResponseModel<out T>(
        @SerializedName("response") val response: T
)