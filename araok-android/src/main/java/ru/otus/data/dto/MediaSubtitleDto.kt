package ru.araok.data.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.MediaSubtitle

@JsonClass(generateAdapter = true)
data class MediaSubtitleDto @RequiresApi(Build.VERSION_CODES.O) constructor(
    @Json(name = "id") override val id: Long? = null,
    @Json(name = "content") override val content: ContentDto = ContentDto(),
    @Json(name = "language") override val language: LanguageDto = LanguageDto(id = null, language = "", code2 = ""),
    @Json(name = "subtitles") override val subtitles: List<SubtitleDto> = emptyList()
): MediaSubtitle
