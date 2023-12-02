package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.JwtRequest

@JsonClass(generateAdapter = true)
data class JwtRequestDto(
    @Json(name = "phone") override val phone: String? = null,
    @Json(name = "password") override val password: String? = null
): JwtRequest
