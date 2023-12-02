package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.JwtResponse

@JsonClass(generateAdapter = true)
data class JwtResponseDto (
    @Json(name = "accessToken") override val accessToken: String? = null,
    @Json(name = "refreshToken") override val refreshToken: String? = null
): JwtResponse