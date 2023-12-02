package ru.araok.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "setting_mark",
    primaryKeys = ["setting_id", "mark_id"]
)
data class SettingsMarksDb(
    @ColumnInfo(name = "setting_id")
    val settingId: Int = 0,
    @ColumnInfo(name = "mark_id")
    val markId: Int = 0
)
