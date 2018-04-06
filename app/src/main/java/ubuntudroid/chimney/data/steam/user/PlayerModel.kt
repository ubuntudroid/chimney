package ubuntudroid.chimney.data.steam.user

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PlayerModel(
        @PrimaryKey
        @SerializedName("steamid")
        val steamId: String,

        @ColumnInfo(name = "communityvisibilitystate")
        @SerializedName("communityvisibilitystate")
        val communityVisibilityState: Int,

        @ColumnInfo(name = "profilestate")
        @SerializedName("profilestate")
        val profileState: Int,

        @ColumnInfo(name = "personaname")
        @SerializedName("personaname")
        val personaName: String,

        @ColumnInfo(name = "lastlogoff")
        @SerializedName("lastlogoff")
        val lastLogoff: Int = -1,

        @ColumnInfo(name = "profileurl")
        @SerializedName("profileurl")
        val profileUrl: String,

        @ColumnInfo(name = "avatar")
        @SerializedName("avatar")
        val avatar: String,

        @ColumnInfo(name = "avatarmedium")
        @SerializedName("avatarmedium")
        val avatarMedium: String,

        @ColumnInfo(name = "avatarfull")
        @SerializedName("avatarfull")
        val avatarFull: String,

        @ColumnInfo(name = "personastate")
        @SerializedName("personastate")
        val personaState: Int,

        @ColumnInfo(name = "realname")
        @SerializedName("realname")
        val realName: String?,

        @ColumnInfo(name = "primaryclanid")
        @SerializedName("primaryclanid")
        val primaryClanId: String?,

        @ColumnInfo(name = "timecreated")
        @SerializedName("timecreated")
        val timeCreated: Int,

        @ColumnInfo(name = "personastateflags")
        @SerializedName("personastateflags")
        val personaStateFlags: Int,

        @ColumnInfo(name = "timestamp")
        val timestamp: Long = -1
)