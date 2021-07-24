package io.github.moh_mohsin.ahoyweatherapp.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun Date.format(pattern: String = "yyyy-MM-dd HH:mm:ss"): String =
    SimpleDateFormat(pattern, Locale.US).format(this)

fun Date.getDateDiff(otherDate: Date, timeUnit: TimeUnit = TimeUnit.DAYS): Long {
    val diffInMillis = this.time - otherDate.time
    return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS)
}

