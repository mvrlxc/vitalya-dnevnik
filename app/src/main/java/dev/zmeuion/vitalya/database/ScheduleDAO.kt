package dev.zmeuion.vitalya.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(schedule: ScheduleDBO)

    @Query("SELECT * FROM schedule")
    fun getAll(): Flow<List<ScheduleDBO>>
}