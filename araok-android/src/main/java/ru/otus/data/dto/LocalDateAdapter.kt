package ru.araok.data.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter {
    @RequiresApi(Build.VERSION_CODES.O)
    @ToJson
    fun toJson(localDate: LocalDate): String {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return localDate.format(format)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @FromJson
    fun fromJson(localDate: String): LocalDate {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(localDate, format)
    }
}