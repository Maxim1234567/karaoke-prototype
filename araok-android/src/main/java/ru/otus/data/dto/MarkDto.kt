package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.Mark

@JsonClass(generateAdapter = true)
data class MarkDto(
    @Json(name = "id") override val id: Int? = null,
    @Json(name = "start") override val start: Int? = null,
    @Json(name = "end") override val end: Int? = null,
    @Json(name = "repeat") override val repeat: Int? = null,
    @Json(name = "delay") override val delay: Int? = null
): Mark