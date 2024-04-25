package dev.zmeuion.vitalya.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [ScheduleDBO::class],
    version = 1
)

abstract class Database : RoomDatabase() {

    abstract fun getScheduleDao(): ScheduleDAO
}