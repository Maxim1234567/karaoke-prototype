package ru.araok.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Insert(entity = SettingsDb::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settingsDb: SettingsDb): Long

    @Insert(entity = MarkDb::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMark(markDb: MarkDb): Long

    @Insert(entity = SettingsMarksDb::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettingsMarks(settingsMarksDb: SettingsMarksDb)

    @Query("select * from setting s where s.content_id = :contentId")
    fun getSettingsWithMarks(contentId: Int): Flow<SettingsWithMarksDb>

    @Query("select * from setting s where s.content_id = :contentId")
    suspend fun loadSettingsWithMarks(contentId: Int): SettingsWithMarksDb

    @Query("select id from setting s where s.content_id = :contentId")
    fun getSettingsId(contentId: Int): Int

    @Query("select mark_id from setting_mark where setting_id = :settingId")
    fun getMarkIds(settingId: Int): List<Int>

    @Query("delete from mark where id in (:markIds)")
    fun deleteMarks(markIds: List<Int>)

    @Query("delete from setting_mark where setting_id = :settingId")
    fun deleteSettingMarks(settingId: Int)

    @Query("delete from setting where id = :settingId")
    fun deleteSettings(settingId: Int)
}