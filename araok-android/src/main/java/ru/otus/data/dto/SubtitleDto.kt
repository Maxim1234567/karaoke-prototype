package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.Subtitle

@JsonClass(generateAdapter = true)
data class SubtitleDto(
    @Json(name = "id") override val id: Long?,
    @Json(name = "line") override val line: String,
    @Json(name = "to") override val to: Long,
    @Json(name = "from") override val from: Long
): Subtitle
