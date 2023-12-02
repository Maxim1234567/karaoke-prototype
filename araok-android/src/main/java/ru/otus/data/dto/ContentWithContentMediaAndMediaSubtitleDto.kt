package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.ContentWithContentMediaAndMediaSubtitle

@JsonClass(generateAdapter = true)
data class ContentWithContentMediaAndMediaSubtitleDto(
    @Json(name = "content") override val content: ContentDto,
    @Json(name = "mediaSubtitle") override val mediaSubtitle: MediaSubtitleDto,
    @Json(name = "preview") override val preview: ContentMediaDto,
    @Json(name = "video") override val video: ContentMediaDto
): ContentWithContentMediaAndMediaSubtitle
