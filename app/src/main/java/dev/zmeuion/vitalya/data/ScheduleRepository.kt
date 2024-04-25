package dev.zmeuion.vitalya.data

import dev.zmeuion.vitalya.data.mappers.toScheduleDBO
import dev.zmeuion.vitalya.database.ScheduleDAO
import dev.zmeuion.vitalya.database.ScheduleDBO
import dev.zmeuion.vitalya.network.api.ScheduleApi
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(
    private val database: ScheduleDAO,
    private val api: ScheduleApi,
) {

    suspend fun loadScheduleToDatabase() {

        val schedule = api.getSchedule().map { it.toScheduleDBO(it) }
        schedule.forEach { database.insert(it) }

    }

    fun getFromDb(): Flow<List<ScheduleDBO>> {
        return database.getAll()
    }

}