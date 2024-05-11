package dev.zmeuion.vitalya.data

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import dev.zmeuion.vitalya.data.mappers.toScheduleDBO
import dev.zmeuion.vitalya.database.DataStoreManager
import dev.zmeuion.vitalya.database.ScheduleDAO
import dev.zmeuion.vitalya.database.ScheduleDBO
import dev.zmeuion.vitalya.network.api.CommentsApi
import dev.zmeuion.vitalya.network.api.ScheduleApi
import dev.zmeuion.vitalya.network.models.CommentDTO
import dev.zmeuion.vitalya.network.models.ScheduleDTO
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(
    private val database: ScheduleDAO,
    private val datastore: DataStoreManager,
    private val api: CommentsApi,
) {

    fun getFromDbByDate(date: String): Flow<List<ScheduleDBO>> {
        return database.getAllByDate(date)
    }

    fun getAll(): Flow<List<ScheduleDBO>> {
        return database.getAllFLow()
    }

    fun getFromDbByDateGroup(date: String, group: String): Flow<List<ScheduleDBO>> {
        return database.getAllByDateAndGroup(date, group)
    }

    fun getGroup(): Flow<String> {
        return datastore.getScheduleFlow()
    }

    suspend fun getScheduleById(id: Int): ScheduleDBO {
        return database.selectByID(id)
    }

    fun getToken(): Flow<String> {
        return datastore.getTokenFlow()
    }

    suspend fun getComments(id: Int): List<CommentDTO> {
        return api.getComments(id)
    }

    suspend fun postComment(lessonID: Int, content: String, lessonDateTime: String): String {
        val token = datastore.getToken()
        return if (token.length > 3) {
            api.postComment(lessonID, token, content, lessonDateTime)
        } else {
            "error"
        }
    }



    suspend fun updateSchedule(newSchedule: List<ScheduleDBO>, context: Context) {
        val oldSchedule = database.getAll()
        var aboba: Int = 0
        // Вставка новых данных, обновление существующих и удаление устаревших
        for (schedule in newSchedule) {
            val existingSchedule = oldSchedule.find { it.id == schedule.id }
            if (existingSchedule != null) {
                if (existingSchedule != schedule) {
                    database.update(schedule)
                    aboba += 1
                }
            } else {
                database.insert(schedule)
                aboba += 1
            }
        }

        for (schedule in oldSchedule) {
            if (newSchedule.none { it.id == schedule.id }) {
                database.delete(schedule)
                aboba += 1
            }
        }
        if (aboba > 0) {
            if (datastore.getNotifications()) {
                makePlantReminderNotification("Расписание изменилось", context)
            }
        }
    }
}

