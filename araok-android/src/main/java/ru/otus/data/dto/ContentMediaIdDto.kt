package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.ContentMediaId

@JsonClass(generateAdapter = true)
data class ContentMediaIdDto(
    @Json(name = "content") override val content: ContentDto,
    @Json(name = "mediaType") override val mediaType: MediaTypeDto
): ContentMediaId
