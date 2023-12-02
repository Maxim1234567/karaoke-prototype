package ru.araok.domain

import android.os.Build
import androidx.annotation.RequiresApi
import ru.araok.consts.TypeContent
import ru.araok.data.AraokRepository
import ru.araok.data.dto.*
import ru.araok.entites.AgeLimit
import ru.araok.entites.Content
import ru.araok.entites.JwtRequest
import javax.inject.Inject

class GetAraokUseCase @Inject constructor(
    private val araokRepository: AraokRepository
) {
    //age limit
    suspend fun getAgeLimit() = araokRepository.getAgeLimit()

    //content
    suspend fun getContents(type: TypeContent) = araokRepository.getContents(type)

    suspend fun getContentsByName(name: String) = araokRepository.getContentsByName(name)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getContentById(accessToken: String, id: Long) = araokRepository.getContentById(accessToken, id)

    suspend fun contentSave(
        accessToken: String,
        content: ContentWithContentMediaAndMediaSubtitleDto
    ) = araokRepository.contentSave(accessToken, content)

    //language
    suspend fun gtAllLanguages() = araokRepository.getAllLanguages()

    suspend fun getAllLanguageSubtitle(accessToken: String, contentId: Long) =
        araokRepository.getAllLanguageSubtitle(accessToken, contentId)

    //media subtitle
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSubtitle(accessToken: String, contentId: Long, languageId: Long) =
        araokRepository.getSubtitle(accessToken, contentId, languageId)

    //media
    suspend fun getMedia(accessToken: String, contentId: Long, typeId: Long = 1) =
        araokRepository.getMedia(accessToken, contentId, typeId)

    //setting
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSetting(accessToken: String, contentId: Long) =
        araokRepository.getSetting(accessToken, contentId)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveSetting(accessToken: String, settings: SettingsDto) =
        araokRepository.saveSetting(accessToken, settings)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateSetting(accessToken: String, settings: SettingsDto) =
        araokRepository.updateSetting(accessToken, settings)

    //authorization
    suspend fun login(jwtRequest: JwtRequestDto) =
        araokRepository.login(jwtRequest)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun registration(user: UserDto) =
        araokRepository.registration(user)

    suspend fun accessToken(refreshJwtRequest: RefreshJwtRequestDto) =
        araokRepository.accessToken(refreshJwtRequest)

    suspend fun refreshToken(accessToken: String, refreshJwtRequest: RefreshJwtRequestDto) =
        araokRepository.refreshToken(accessToken, refreshJwtRequest)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getUser(accessToken: String) =
        araokRepository.getUser(accessToken)
}