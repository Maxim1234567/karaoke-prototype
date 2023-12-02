package ru.araok.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.araok.data.dto.MarkDto
import ru.araok.entites.Settings

@Entity(tableName = "setting")
data class SettingsDb(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Int? = null,
    @ColumnInfo(name = "content_id")
    override val contentId: Int? = null
): Settings