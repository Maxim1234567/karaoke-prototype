package ru.araok.domain

import ru.araok.data.SettingsDbRepository
import ru.araok.data.db.MarkDb
import ru.araok.data.db.SettingsDb
import ru.araok.data.db.SettingsMarksDb
import ru.araok.data.db.SettingsWithMarksDb
import javax.inject.Inject

class GetAraokDbUseCase @Inject constructor(
    private val settingsDbRepository: SettingsDbRepository
) {
    suspend fun insertSettingWithMarks(settingsWithMarksDb: SettingsWithMarksDb) {
        settingsDbRepository.insertSettingWithMarks(settingsWithMarksDb)
    }

    fun deleteSettings(contentId: Int) {
        settingsDbRepository.deleteSettings(contentId)
    }

    fun getSettingsWithMarks(contentId: Int) = settingsDbRepository.getSettingsWithMarks(contentId)

    suspend fun loadSettingsWithMarks(contentId: Int) = settingsDbRepository.loadSettingsWithMarks(contentId)
}