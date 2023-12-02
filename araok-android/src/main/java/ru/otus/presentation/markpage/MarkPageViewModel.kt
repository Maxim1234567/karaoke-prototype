package ru.araok.presentation.markpage

import android.content.Context
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
import ru.araok.data.db.SettingsDb
import ru.araok.data.db.SettingsWithMarksDb
import ru.araok.data.dto.ContentDto
import ru.araok.data.dto.MarkDto
import ru.araok.data.dto.SettingsDto
import ru.araok.domain.GetAraokDbUseCase
import ru.araok.domain.GetAraokUseCase
import ru.araok.entites.SettingsWithMarks
import javax.inject.Inject
import kotlin.streams.toList

@RequiresApi(Build.VERSION_CODES.O)
class MarkPageViewModel @Inject constructor(
    private val getAraokDbUseCase: GetAraokDbUseCase,
    private val getAraokUseCase: GetAraokUseCase
): ViewModel() {
    private val _load = MutableStateFlow(State.EMPTY)
    val load: StateFlow<State> = _load.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _load.value
    )

    private var _settingsDb = MutableStateFlow(
        SettingsWithMarksDb(settingDb = SettingsDb(id = -1, contentId = -1))
    )
    val settingsDb: StateFlow<SettingsWithMarks> = _settingsDb.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _settingsDb.value
    )

    private var _settings = MutableStateFlow(SettingsDto(id = -1))
    val settings: StateFlow<SettingsDto> = _settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _settings.value
    )

    fun loadSettingsDb(contentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokDbUseCase.loadSettingsWithMarks(contentId)
            }.fold(
                onSuccess = { _settingsDb.value = it },
                onFailure = { Log.d("VideoPageViewModel", "Error Load Settings Db ${it.message ?: ""}")}
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun addSettingsWithMarksOnlyDb(settingsWithMarksDb: SettingsWithMarksDb) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _load.value = State.PROCESS
                getAraokDbUseCase.deleteSettings(settingsWithMarksDb.settingDb.contentId!!)
                getAraokDbUseCase.insertSettingWithMarks(settingsWithMarksDb)
            }.fold(
                onSuccess = { _load.value = State.LOAD },
                onFailure = { Log.d("MarkPageViewModel", it.message ?: "Error") }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addSettingsWithMarks(context: Context, settingsWithMarksDb: SettingsWithMarksDb) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _load.value = State.PROCESS
                getAraokDbUseCase.deleteSettings(settingsWithMarksDb.settingDb.contentId!!)
                getAraokDbUseCase.insertSettingWithMarks(settingsWithMarksDb)
                getAraokUseCase.saveSetting(
                    Repository.getAccessToken(context),
                    SettingsDto(
                    content = ContentDto(id = settingsWithMarksDb.settingDb.contentId.toLong()),
                    marks = settingsWithMarksDb.marksDb.stream().map { MarkDto(
                        start = it.start,
                        end = it.end,
                        repeat = it.repeat,
                        delay = it.delay
                    ) }.toList())
                )
            }.fold(
                onSuccess = { _load.value = State.LOAD },
                onFailure = { Log.d("MarkPageViewModel", it.message ?: "Error") }
            )
        }
    }
}