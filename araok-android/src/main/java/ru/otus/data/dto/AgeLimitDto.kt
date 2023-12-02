package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.AgeLimit

@JsonClass(generateAdapter = true)
data class AgeLimitDto(
    @Json(name = "id") override val id: Int?,
    @Json(name = "description") override val description: String,
    @Json(name = "limit") override val limit: Long
): AgeLimit