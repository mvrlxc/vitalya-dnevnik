package dev.zmeuion.vitalya.data

import android.content.Context
import android.net.http.HttpException
import androidx.room.Dao
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.zmeuion.vitalya.data.mappers.toScheduleDBO
import dev.zmeuion.vitalya.database.DataStoreManager
import dev.zmeuion.vitalya.database.ScheduleDAO
import dev.zmeuion.vitalya.network.api.ScheduleApi
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getKoin
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/*
class UpdateScheduleWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(
    context, params,
), KoinComponent {
    private val api: ScheduleApi by inject<ScheduleApi>()
    private val database: ScheduleDAO by inject<ScheduleDAO>()
    private val repo: ScheduleRepository by inject<ScheduleRepository>()
    override suspend fun doWork(): Result {
        return try {
            val schedule = api.getSchedule()
        //    database.delete()
            schedule.forEach { database.update(it.toScheduleDBO(it)) }
            Result.retry()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}


 */
class UpdateScheduleWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(
    context, params,
), KoinComponent {
    private val api: ScheduleApi by inject<ScheduleApi>()
    private val database: ScheduleDAO by inject<ScheduleDAO>()
    private val repo: ScheduleRepository by inject<ScheduleRepository>()
    override suspend fun doWork(): Result {
        return try {
            val schedule = api.getSchedule().map { it.toScheduleDBO(it) }
            repo.updateSchedule(schedule)
            Result.retry()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}