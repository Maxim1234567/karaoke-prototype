package ru.araok.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ru.araok.consts.TypeContent
import ru.araok.data.dto.*
import ru.araok.entites.*
import javax.inject.Inject

private const val BEARER = "Bearer "

class AraokRepository @Inject constructor() {
    //age limit
    suspend fun getAgeLimit(): List<AgeLimit> =
        RetrofitService.araokApi.getAgeLimit().body() ?: emptyList()

    //content
    suspend fun getContents(type: TypeContent): List<Content> =
        RetrofitService.araokApi.getContents(type.name).body() ?: emptyList()

    suspend fun getContentsByName(name: String): List<Content> =
        RetrofitService.araokApi.getContentsByName(name).body() ?: emptyList()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getContentById(accessToken: String, id: Long): Content =
        RetrofitService.araokApi.getContentById(BEARER + accessToken, id).body() ?: ContentDto()

    suspend fun contentSave(accessToken: String, content: ContentWithContentMediaAndMediaSubtitleDto): String =
        RetrofitService.araokApi.contentSave(BEARER + accessToken, content).body() ?: "Not OK"

    //language
    suspend fun getAllLanguages(): List<Language> =
        RetrofitService.araokApi.getAllLanguages().body() ?: emptyList()

    suspend fun getAllLanguageSubtitle(accessToken: String, contentId: Long) =
        RetrofitService.araokApi.getAllLanguageSubtitle(BEARER + accessToken, contentId).body() ?: emptyList()

    //media subtitle
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSubtitle(accessToken: String, contentId: Long, languageId: Long) =
        RetrofitService.araokApi.getSubtitle(BEARER + accessToken, contentId, languageId).body() ?: MediaSubtitleDto()

    //media
    suspend fun getMedia(accessToken: String, contentId: Long, typeId: Long) =
        RetrofitService.araokApi.getMedia(BEARER + accessToken, contentId, typeId).bytes().asList()

    //setting
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSetting(accessToken: String, contentId: Long) =
        RetrofitService.araokApi.getSetting(BEARER + accessToken, contentId).body() ?: SettingsDto()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveSetting(accessToken: String, settings: SettingsDto) =
        RetrofitService.araokApi.settingSave(BEARER + accessToken, settings).body() ?: SettingsDto()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateSetting(accessToken: String, settings: SettingsDto) =
        RetrofitService.araokApi.settingUpdate(BEARER + accessToken, settings).body() ?: SettingsDto()

    //authorization
    suspend fun login(jwtRequest: JwtRequestDto) =
        RetrofitService.araokApi.login(jwtRequest).body() ?: JwtResponseDto()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun registration(user: UserDto) =
        RetrofitService.araokApi.registration(user).body() ?: UserWithJwtResponseDto(
            user = UserDto(),
            token = JwtResponseDto()
        )

    suspend fun accessToken(refreshJwtRequest: RefreshJwtRequestDto) =
        RetrofitService.araokApi.accessToken(refreshJwtRequest)

    suspend fun refreshToken(accessToken: String, refreshJwtRequest: RefreshJwtRequestDto) =
        RetrofitService.araokApi.refreshToken(BEARER + accessToken, refreshJwtRequest)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getUser(accessToken: String) =
        RetrofitService.araokApi.getUser(BEARER + accessToken).body() ?: UserDto()
}