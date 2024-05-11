package dev.zmeuion.vitalya.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.http.HttpException
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Dao
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.zmeuion.vitalya.MainActivity
import dev.zmeuion.vitalya.R
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
    private val contextX = context
    override suspend fun doWork(): Result {
        return try {
            val schedule = api.getSchedule().map { it.toScheduleDBO(it) }
            repo.updateSchedule(schedule, contextX)
            Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}


fun makePlantReminderNotification(
    message: String,
    context: Context
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            CHANNEL_ID,
            VERBOSE_NOTIFICATION_CHANNEL_NAME,
            importance
        )
        channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    val pendingIntent: PendingIntent = createPendingIntent(context)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}

fun createPendingIntent(appContext: Context): PendingIntent {
    val intent = Intent(appContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    // Flag to detect unsafe launches of intents for Android 12 and higher
    // to improve platform security
    var flags = PendingIntent.FLAG_UPDATE_CURRENT
    flags = flags or PendingIntent.FLAG_IMMUTABLE

    return PendingIntent.getActivity(
        appContext,
        REQUEST_CODE,
        intent,
        flags
    )
}

// Name of Notification Channel for verbose notifications of background work
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Verbose WorkManager Notifications"

// Description of Notification Channel for verbose notifications of background work
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"

// Title of Notification for verbose notifications of background work
val NOTIFICATION_TITLE: CharSequence = "Дневник витали!"

// ID of Notification Channel for verbose notifications of background work
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"

// ID of Notification for verbose notifications of background work
const val NOTIFICATION_ID = 1

// Request code for pending intent
const val REQUEST_CODE = 0