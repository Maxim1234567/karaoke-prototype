package ru.araok.data.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SettingsDto @RequiresApi(Build.VERSION_CODES.O) constructor(
    @Json(name = "id") val id: Long? = null,
    @Json(name = "content") val content: ContentDto = ContentDto(),
    @Json(name = "marks") val marks: List<MarkDto> = emptyList()
)