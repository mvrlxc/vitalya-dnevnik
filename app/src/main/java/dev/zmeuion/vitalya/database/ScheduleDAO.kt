package dev.zmeuion.vitalya.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDAO {

    @Delete
    suspend fun delete(schedule: ScheduleDBO)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(schedule: ScheduleDBO)

    @Update()
    suspend fun update(schedule: ScheduleDBO)

    @Query("SELECT * FROM schedule")
    suspend fun getAll(): List<ScheduleDBO>

    @Query("SELECT * FROM schedule WHERE date = :date")
    fun getAllByDate(date: String): Flow<List<ScheduleDBO>>

    @Query("SELECT * FROM schedule WHERE date = :date AND `group` = :group")
    fun getAllByDateAndGroup(date: String, group: String): Flow<List<ScheduleDBO>>
}