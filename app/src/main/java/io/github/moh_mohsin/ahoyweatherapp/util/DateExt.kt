package io.github.moh_mohsin.ahoyweatherapp.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun Date.format(pattern: String = "yyyy-MM-dd HH:mm:ss") =
    SimpleDateFormat(pattern, Locale.US).format(this)

fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss") =
    SimpleDateFormat(pattern, Locale.US).parse(this) ?: error("Invalid Date: $pattern")

fun Date.getDateDiff(otherDate: Date, timeUnit: TimeUnit = TimeUnit.DAYS): Long {
    val diffInMillis = this.time - otherDate.time
    return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS)
}

fun Date.diffFrom(fromDate: Date, timeUnit: TimeUnit): Long {

    val diffInMillies = this.getDateOnly().time - fromDate.getDateOnly().time
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
}

fun Date.toReadableFormat(): String {
    return if (diffFrom(Date(), TimeUnit.DAYS) > -1) {
        format("hh:mm aa")
    } else {
        format("d MMM yyyy hh:mm aa")
    }
}

fun Date.toReadableDateOnly(): String = format("yyyy-MM-dd")

fun Date.toReadableFormatDate(): String = format("d MMM yyyy")

fun Date.getDateOnly(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.time
}