package ru.araok.data

import ru.araok.data.db.*
import javax.inject.Inject

class SettingsDbRepository @Inject constructor(
    private val settingsDao: SettingsDao
) {
    suspend fun insertSettingWithMarks(settingsWithMarksDb: SettingsWithMarksDb) {
        val settingId = settingsDao.insertSettings(settingsWithMarksDb.settingDb)
        settingsWithMarksDb.marksDb.forEach {
            val markId = settingsDao.insertMark(it)
            settingsDao.insertSettingsMarks(
                SettingsMarksDb(
                    settingId = settingId.toInt(),
                    markId = markId.toInt()
                )
            )
        }
    }

    fun deleteSettings(contentId: Int) {
        val settingId = settingsDao.getSettingsId(contentId)
        val markIds = settingsDao.getMarkIds(settingId)

        settingsDao.deleteMarks(markIds)
        settingsDao.deleteSettingMarks(settingId)
        settingsDao.deleteSettings(settingId)
    }

    fun getSettingsWithMarks(contentId: Int) = settingsDao.getSettingsWithMarks(contentId)

    suspend fun loadSettingsWithMarks(contentId: Int) = settingsDao.loadSettingsWithMarks(contentId)
}