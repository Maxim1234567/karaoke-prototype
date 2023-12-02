package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.Language

@JsonClass(generateAdapter = true)
data class LanguageDto(
    @Json(name = "id") override val id: Int?,
    @Json(name = "language") override val language: String,
    @Json(name = "code2") override val code2: String
): Language
