package ubuntudroid.chimney.network.steam.user

import com.google.gson.annotations.SerializedName

data class PlayerModel(
        @SerializedName("steamid") val steamId: String,
        @SerializedName("communityvisibilitystate") val communityVisibilityState: Int,
        @SerializedName("profilestate") val profileState: Int,
        @SerializedName("personaname") val personaName: String,
        @SerializedName("lastlogoff") val lastLogoff: Int,
        @SerializedName("profileurl") val profileUrl: String,
        @SerializedName("avatar") val avatar: String,
        @SerializedName("avatarmedium") val avatarMedium: String,
        @SerializedName("avatarfull") val avatarFull: String,
        @SerializedName("personastate") val personaState: Int,
        @SerializedName("realname") val realName: String,
        @SerializedName("primaryclanid") val primaryClanId: String,
        @SerializedName("timecreated") val timeCreated: Int,
        @SerializedName("personastateflags") val personaStateFlags: Int
)