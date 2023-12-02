package ru.araok.data.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.User
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class UserDto @RequiresApi(Build.VERSION_CODES.O) constructor(
    @Json(name = "id") override val id: Long = -1,
    @Json(name = "name") override val name: String = "",
    @Json(name = "phone") override val phone: String = "",
    @Json(name = "password") override val password: String = "",
    @Json(name = "birthDate") override val birthDate: LocalDate = LocalDate.now(),
    @Json(name = "role") override val role: String = ""
): User
