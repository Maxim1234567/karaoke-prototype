package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.MediaType

@JsonClass(generateAdapter = true)
data class MediaTypeDto(
    @Json(name = "id") override val id: Long?,
    @Json(name = "type") override val type: String
): MediaType
