package ubuntudroid.chimney.data.steam.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ubuntudroid.chimney.data.steam.user.PlayerModel
import ubuntudroid.chimney.data.steam.user.db.SteamUserDao

@Database(entities = [PlayerModel::class], version = 1)
abstract class SteamDatabase: RoomDatabase() {

    abstract fun steamUserDao(): SteamUserDao
}