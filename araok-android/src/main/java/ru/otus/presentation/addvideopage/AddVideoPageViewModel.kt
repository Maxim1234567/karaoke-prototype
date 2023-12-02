package ru.araok.presentation.addvideopage

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.araok.data.Repository
import ru.araok.data.dto.*
import ru.araok.domain.GetAraokUseCase
import ru.araok.entites.AgeLimit
import java.time.LocalDate
import java.util.Objects
import javax.inject.Inject

const val VIDEO_PAGE_VIEW_MODEL_TAG = "VideoPageViewModel"

class AddVideoPageViewModel @Inject constructor(
    private val getAraokUseCase: GetAraokUseCase
): ViewModel() {
    var pathMedia: Uri? = null
    var pathPreview: Uri? = null
    lateinit var pathSubtitleOriginal: String
    lateinit var pathSubtitleTranslate: String
    lateinit var artist: String
    lateinit var songName: String
    lateinit var ageLimit: AgeLimitDto

    private val _ageLimits = MutableStateFlow<List<AgeLimit>>(emptyList())
    val ageLimits: StateFlow<List<AgeLimit>> = _ageLimits.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _ageLimits.value
    )

    private val _content = MutableStateFlow("EMPTY")
    val content: StateFlow<String> = _content.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _content.value
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadMedia(context: Context, contentResolver: ContentResolver) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val user = UserDto(
                    id = 1,
                    name = "Maxim",
                    phone = "89993338951",
                    password = "12345",
                    birthDate = LocalDate.of(1994, 5, 8),
                    role = "USER"
                )

                val language = LanguageDto(
                    id = 1,
                    language = "Russian",
                    code2 = "RU"
                )

                val content = ContentDto(
                    id = null,
                    name = songName,
                    limit = ageLimit,
                    artist = artist,
                    user = user,
                    createDate = LocalDate.now(),
                    language = language
                )

                val mediaSubtitleOriginal = MediaSubtitleDto(
                    id = null,
                    content = content,
                    language = language,
                    subtitles = FileUtils.fileToSubtitles(pathSubtitleOriginal)
                )

                val previewId = ContentMediaIdDto(
                    content = content,
                    mediaType = MediaTypeDto(
                        id = 3L,
                        type = "IMAGE"
                    )
                )

                val previewInputStream = contentResolver.openInputStream(pathPreview!!)
                val previewBytes = FileUtils.getBytes(previewInputStream)
                val preview = ContentMediaDto(
                    contentMediaId = previewId,
                    media = previewBytes
                )

                val mediaId = ContentMediaIdDto(
                    content = content,
                    mediaType = MediaTypeDto(
                        id = 1L,
                        type = "VIDEO"
                    )
                )

                val mediaInputStream = contentResolver.openInputStream(pathMedia!!)
                val mediaBytes = FileUtils.getBytes(mediaInputStream)
                val media = ContentMediaDto(
                    contentMediaId = mediaId,
                    media = mediaBytes
                )

                val fullContent = ContentWithContentMediaAndMediaSubtitleDto(
                    content = content,
                    mediaSubtitle = mediaSubtitleOriginal,
                    preview = preview,
                    video = media
                )

                getAraokUseCase.contentSave(Repository.getAccessToken(context), fullContent)
            }.fold(
                onSuccess = { _content.value = it },
                onFailure = { _content.value = "FAILURE"; Log.d(VIDEO_PAGE_VIEW_MODEL_TAG, "uploadMedia ${it.message ?: "Error"}"); }
            )
        }
    }

    fun loadAgeLimit() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getAgeLimit()
            }.fold(
                onSuccess = { _ageLimits.value = it },
                onFailure = { Log.d(VIDEO_PAGE_VIEW_MODEL_TAG, it.message ?: "Error") }
            )
        }
    }

    fun isFullData(): Boolean =
        Objects.nonNull(pathMedia) && Objects.nonNull(pathPreview) && pathSubtitleOriginal.isNotBlank() &&
        pathSubtitleTranslate.isNotBlank() && artist.isNotBlank() && songName.isNotBlank() &&
        Objects.nonNull(ageLimit)
}