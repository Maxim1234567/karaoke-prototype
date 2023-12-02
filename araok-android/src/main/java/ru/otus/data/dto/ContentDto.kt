package ru.araok.data.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.araok.entites.Content
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class ContentDto @RequiresApi(Build.VERSION_CODES.O) constructor(
    @Json(name = "id") override val id: Long? = null,
    @Json(name = "name") override val name: String = "",
    @Json(name = "limit") override val limit: AgeLimitDto = AgeLimitDto(id = null, description = "", limit = 0),
    @Json(name = "artist") override val artist: String = "",
    @Json(name = "user") override val user: UserDto = UserDto(id = 0, name = "", phone = "", password = "", LocalDate.now(), role = ""),
    @Json(name = "createDate") override val createDate: LocalDate = LocalDate.now(),
    @Json(name = "language") override val language: LanguageDto = LanguageDto(id = null, language = "", code2 = "")
): Content
