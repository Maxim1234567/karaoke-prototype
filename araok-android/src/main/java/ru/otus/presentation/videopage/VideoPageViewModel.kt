package ru.araok.presentation.videopage

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.araok.data.Repository
import ru.araok.data.db.SettingsWithMarksDb
import ru.araok.data.dto.SettingsDto
import ru.araok.domain.GetAraokDbUseCase
import ru.araok.domain.GetAraokUseCase
import ru.araok.entites.Language
import ru.araok.entites.Subtitle
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class VideoPageViewModel @Inject constructor(
    private val getAraokUseCase: GetAraokUseCase,
    private val getAraokDbUseCase: GetAraokDbUseCase
): ViewModel() {
    private val _languageFlow = MutableStateFlow(-1)
    val languageFlow = _languageFlow.asStateFlow()

    val selectLanguage: Int
        get() = _languageFlow.value

    private var _video: Channel<ByteArray> = Channel()
    val video: Flow<ByteArray> = _video.receiveAsFlow()

    private val _language = MutableStateFlow<List<Language>>(emptyList())
    val language: StateFlow<List<Language>> = _language.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _language.value
    )

    private val _subtitle = MutableStateFlow<List<Subtitle>>(emptyList())
    val subtitle: StateFlow<List<Subtitle>> = _subtitle.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _subtitle.value
    )

    private var _settingsDb: Channel<SettingsWithMarksDb> = Channel()
    val settingsDb: Flow<SettingsWithMarksDb> = _settingsDb.receiveAsFlow()

    private var _settings = MutableStateFlow(SettingsDto(id = -1))
    val settings: StateFlow<SettingsDto> = _settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _settings.value
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadSubtitle(context: Context, contentId: Long, languageId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getSubtitle(Repository.getAccessToken(context), contentId, languageId)
            }.fold(
                onSuccess = { _subtitle.value = it.subtitles },
                onFailure = { Log.d("VideoPageViewModel", it.message ?: "") }
            )
        }
    }

    fun loadVideo(context: Context, contentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getMedia(Repository.getAccessToken(context), contentId)
            }.fold(
                onSuccess = { _video.send(it.toByteArray()) },
                onFailure = { Log.d("VideoPageViewModel", it.message ?: "") }
            )
        }
    }

    fun loadSettingsDb(contentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokDbUseCase.loadSettingsWithMarks(contentId.toInt())
            }.fold(
                onSuccess = { _settingsDb.send(it) },
                onFailure = { Log.d("VideoPageViewModel", it.message ?: "")}
            )
        }
    }

    fun loadSettings(context: Context, contentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getSetting(Repository.getAccessToken(context), contentId)
            }.fold(
                onSuccess = { _settings.value = it },
                onFailure = { Log.d("VideoPageViewModel", "Error Load Setting: ${it.message ?: ""}")}
            )
        }
    }

    fun getAllLanguageSubtitle(context: Context, contentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getAllLanguageSubtitle(Repository.getAccessToken(context), contentId)
            }.fold(
                onSuccess = { _language.value = it },
                onFailure = { Log.d("SubtitleDialogViewModel", it.message ?: "") }
            )
        }
    }

    fun addSettingsWithMarks(settingsWithMarksDb: SettingsWithMarksDb) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokDbUseCase.deleteSettings(settingsWithMarksDb.settingDb.contentId!!)
                getAraokDbUseCase.insertSettingWithMarks(settingsWithMarksDb)
            }.fold(
                onSuccess = { Log.d("MarkPageViewModel", "onSuccess") },
                onFailure = { Log.d("MarkPageViewModel", it.message ?: "Error") }
            )
        }
    }

    fun sendLanguageId(languageId: Int) {
        viewModelScope.launch {
            if(_languageFlow.value != languageId)
                _languageFlow.value = languageId
        }
    }
}