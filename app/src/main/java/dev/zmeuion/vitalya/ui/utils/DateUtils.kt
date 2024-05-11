package dev.zmeuion.vitalya.ui.utils

import android.os.Build
import androidx.annotation.RequiresApi
import dev.zmeuion.vitalya.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DateView(
    val dayOfWeek: Int,
    val day: Int,
    val month: Int,
)

fun formatDate(inputDate: String): DateView {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date = dateFormat.parse(inputDate)
    val calendar = Calendar.getInstance()
    calendar.time = date ?: Date()

    val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> R.string.sunday
        Calendar.MONDAY -> R.string.monday
        Calendar.TUESDAY -> R.string.tuesday
        Calendar.WEDNESDAY -> R.string.wednesday
        Calendar.THURSDAY -> R.string.thursday
        Calendar.FRIDAY -> R.string.friday
        Calendar.SATURDAY -> R.string.saturday
        else -> 0
    }

    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    val month = when (calendar.get(Calendar.MONTH)) {
        Calendar.JANUARY -> R.string.january
        Calendar.FEBRUARY -> R.string.february
        Calendar.MARCH -> R.string.march
        Calendar.APRIL -> R.string.april
        Calendar.MAY -> R.string.may
        Calendar.JUNE -> R.string.june
        Calendar.JULY -> R.string.july
        Calendar.AUGUST -> R.string.august
        Calendar.SEPTEMBER -> R.string.september
        Calendar.OCTOBER -> R.string.october
        Calendar.NOVEMBER -> R.string.november
        Calendar.DECEMBER -> R.string.december
        else -> 0
    }

    return DateView(dayOfWeek = dayOfWeek, day = dayOfMonth, month = month)
}

fun formatDateFromMillis(millis: Long): String {
    val date = Date(millis)
    val formatter = SimpleDateFormat("dd.MM.yyyy")
    return formatter.format(date)
}

fun isDateValid(date: String): Boolean {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    dateFormat.isLenient = false
    try {
        // Пытаемся распарсить строку как дату
        dateFormat.parse(date)
        return true
    } catch (e: ParseException) {
        // Если происходит ParseException, значит строка не соответствует формату даты
        return false
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return currentDate.format(formatter)
}