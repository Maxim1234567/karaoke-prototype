package ru.araok.data

import retrofit2.Retrofit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.araok.data.dto.*
import ru.araok.entites.JwtResponse
import ru.araok.entites.RefreshJwtRequest

const val BASE_URL = "http://10.0.2.2:8765"

object RetrofitService {
    private val adapter = Moshi.Builder()
        .add(LocalDateAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient
                .Builder()
                .addInterceptor(AuthorizationResponseInterceptor())
                .build()
        )
        .addConverterFactory(
            MoshiConverterFactory.create(adapter).asLenient()
        )
        .build()

    val araokApi: AraokApi = retrofit.create(
        AraokApi::class.java
    )

    interface AraokApi {
        //age-limit
        @GET("/api/limit")
        suspend fun getAgeLimit(): Response<List<AgeLimitDto>>

        //content
        @GET("/api/content")
        suspend fun getContents(@Query("type") type: String): Response<List<ContentDto>>

        @GET("/api/content/{name}")
        suspend fun getContentsByName(@Path("name") name: String): Response<List<ContentDto>>

        @GET("/api/content/id/{id}")
        suspend fun getContentById(
            @Header("Authorization") accessToken: String,
            @Path("id") id: Long
        ): Response<ContentDto>

        @POST("/api/content")
        suspend fun contentSave(
            @Header("Authorization") accessToken: String,
            @Body content: ContentWithContentMediaAndMediaSubtitleDto
        ): Response<String>

        //language
        @GET("/api/language")
        suspend fun getAllLanguages(): Response<List<LanguageDto>>

        //media subtitle
        @GET("/api/subtitle/{contentId}/{languageId}")
        suspend fun getSubtitle(
            @Header("Authorization") accessToken: String,
            @Path("contentId") contentId: Long,
            @Path("languageId") languageId: Long
        ): Response<MediaSubtitleDto>

        @GET("/api/subtitle/{contentId}")
        suspend fun getAllLanguageSubtitle(
            @Header("Authorization") accessToken: String,
            @Path("contentId") contentId: Long
        ): Response<List<LanguageDto>>

        //media
        @GET("/api/media/{contentId}/{typeId}")
        @Headers("Content-Type: application/octet-stream")
        suspend fun getMedia(
            @Header("Authorization") accessToken: String,
            @Path("contentId") contentId: Long,
            @Path("typeId") typeId: Long
        ): ResponseBody

        //setting
        @GET("/api/setting/{contentId}")
        suspend fun getSetting(
            @Header("Authorization") accessToken: String,
            @Path("contentId") contentId: Long
        ): Response<SettingsDto>

        @POST("/api/setting")
        suspend fun settingSave(
            @Header("Authorization") accessToken: String,
            @Body settings: SettingsDto
        ): Response<SettingsDto>

        @PUT("/api/setting")
        suspend fun settingUpdate(
            @Header("Authorization") accessToken: String,
            @Body settings: SettingsDto
        ): Response<SettingsDto>

        //authorization
        @POST("/auth/login")
        suspend fun login(@Body jwtRequest: JwtRequestDto): Response<JwtResponseDto>

        @POST("/auth/registration")
        suspend fun registration(@Body user: UserDto): Response<UserWithJwtResponseDto>

        @POST("/auth/token")
        suspend fun accessToken(@Body refreshJwtRequest: RefreshJwtRequestDto): Response<JwtResponseDto>

        @POST("/auth/refresh")
        suspend fun refreshToken(
            @Header("Authorization") accessToken: String,
            @Body refreshJwtRequest: RefreshJwtRequestDto
        ): Response<JwtResponseDto>

        @GET("/auth/user")
        suspend fun getUser(
            @Header("Authorization") accessToken: String
        ): Response<UserDto>
    }
}