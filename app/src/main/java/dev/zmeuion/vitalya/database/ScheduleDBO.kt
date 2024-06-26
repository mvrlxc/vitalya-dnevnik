package dev.zmeuion.vitalya.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class ScheduleDBO(
    @PrimaryKey
    val id: Int,
    val group: String,
    val timeStart: String,
    val timeEnd: String,
    val name: String,
    val type: String,
    val place: String,
    val teacher: String,
    val date: String,
    val pairNumber: Int,
    val homework: String,
)

//id
//time_start
//time_end
//name
//type
//place
//teacher
//date
//pair_number