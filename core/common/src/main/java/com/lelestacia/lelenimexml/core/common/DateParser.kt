package com.lelestacia.lelenimexml.core.common

import com.lelestacia.lelenimexml.core.common.Constant.UNKNOWN
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class DateParser {
    operator fun invoke(unformattedDate: String?): String {
        return if (unformattedDate.isNullOrEmpty()) UNKNOWN
        else {
            val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val date: Date = Date.from(Instant.from(formatter.parse(unformattedDate)))
            val pattern = "dd MMMM yyyy"
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            dateFormat.format(date)
        }
    }
}
