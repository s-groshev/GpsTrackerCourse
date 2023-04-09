package com.nosta.gpstrackercourse.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    private val timeFormatter = SimpleDateFormat("HH:mm:ss:SSS")

    fun getTime(timeInMillis: Long): String{
        val cv = Calendar.getInstance()
        timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
        cv.timeInMillis = timeInMillis
        return timeFormatter.format(cv.time)
    }

}