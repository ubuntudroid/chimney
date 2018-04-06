package ubuntudroid.chimney.db.steam.user

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import ubuntudroid.chimney.data.steam.user.PlayerModel

@Dao
abstract class SteamUserDao {
    @Query("SELECT * FROM PlayerModel")
    abstract fun getAll(): LiveData<List<PlayerModel>>

    @Query("SELECT * FROM PlayerModel WHERE steamId IN (:steamId)")
    abstract fun getById(steamId: String): LiveData<PlayerModel>

    @Delete
    abstract fun delete(playerModel: PlayerModel)

    @Insert
    abstract fun insert(playerModel: PlayerModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertAndIgnoreConflicts(playerModel: PlayerModel):Long

    @Update
    protected abstract fun update(playerModel: PlayerModel)

    fun upsert(playerModel: PlayerModel) {
        val id = insertAndIgnoreConflicts(playerModel)
        if (id == -1L) {
            update(playerModel)
        }
    }
}