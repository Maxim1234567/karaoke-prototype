package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.RefreshJwtRequest

@JsonClass(generateAdapter = true)
data class RefreshJwtRequestDto(
    @Json(name = "refreshToken") override val refreshToken: String?
): RefreshJwtRequest
