package ru.araok.data.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.araok.entites.SettingsWithMarks

data class SettingsWithMarksDb(
    @Embedded
    override val settingDb: SettingsDb = SettingsDb(),
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            SettingsMarksDb::class,
            parentColumn = "setting_id",
            entityColumn = "mark_id"
        )
    )
    override val marksDb: List<MarkDb> = emptyList()
): SettingsWithMarks