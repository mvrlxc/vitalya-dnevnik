package dev.zmeuion.vitalya.data

import dev.zmeuion.vitalya.data.mappers.toScheduleDBO
import dev.zmeuion.vitalya.database.DataStoreManager
import dev.zmeuion.vitalya.database.ScheduleDAO
import dev.zmeuion.vitalya.database.ScheduleDBO
import dev.zmeuion.vitalya.network.api.ScheduleApi
import dev.zmeuion.vitalya.network.models.ScheduleDTO
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(
    private val database: ScheduleDAO,
    private val datastore: DataStoreManager,
) {

    fun getFromDbByDate(date: String): Flow<List<ScheduleDBO>> {
        return database.getAllByDate(date)
    }

    fun getFromDbByDateGroup(date: String, group: String): Flow<List<ScheduleDBO>> {
        return database.getAllByDateAndGroup(date, group)
    }

    fun getGroup(): Flow<String> {
        return datastore.getScheduleFlow()
    }

    suspend fun updateSchedule(newSchedule: List<ScheduleDBO>) {
        val oldSchedule = database.getAll()

        for (schedule in newSchedule) {
            if (!oldSchedule.map { it.toMatch(it) }.contains(schedule.toMatch(schedule))) {
                database.insert(schedule)
            }
        }

        for (schedule in oldSchedule) {
            if (newSchedule.map { it.toMatch(it) }.none { it == schedule.toMatch(schedule) }) {
                database.delete(schedule)
            }
        }


    }
}

data class MatchSchedule(
    val group: String,
    val timeStart: String,
    val timeEnd: String,
    val name: String,
    val type: String,
    val place: String,
    val teacher: String,
    val date: String,
    val pairNumber: String,
)

fun ScheduleDBO.toMatch(scheduleDBO: ScheduleDBO): MatchSchedule {
    return MatchSchedule(
        scheduleDBO.group,
        scheduleDBO.timeStart,
        scheduleDBO.timeEnd,
        scheduleDBO.name,
        scheduleDBO.type,
        scheduleDBO.place,
        scheduleDBO.teacher,
        scheduleDBO.date,
        scheduleDBO.pairNumber
    )
}