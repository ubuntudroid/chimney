package ubuntudroid.chimney.db.steam

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ubuntudroid.chimney.data.steam.user.PlayerModel
import ubuntudroid.chimney.db.steam.user.SteamUserDao

@Database(entities = [PlayerModel::class], version = 1)
abstract class SteamDatabase: RoomDatabase() {

    abstract fun steamUserDao(): SteamUserDao
}