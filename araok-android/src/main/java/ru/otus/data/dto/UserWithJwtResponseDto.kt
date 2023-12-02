package ru.araok.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.UserWithJwtResponse

@JsonClass(generateAdapter = true)
data class UserWithJwtResponseDto constructor(
    @Json(name = "user") override val user: UserDto,
    @Json(name = "token") override val token: JwtResponseDto
): UserWithJwtResponse