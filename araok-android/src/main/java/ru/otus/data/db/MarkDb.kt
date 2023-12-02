package ru.araok.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.araok.entites.Mark

@Entity(tableName = "mark")
data class MarkDb(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Int? = null,
    @ColumnInfo(name = "start")
    override val start: Int? = null,
    @ColumnInfo(name = "end")
    override val end: Int? = null,
    @ColumnInfo(name = "repeat")
    override val repeat: Int? = null,
    @ColumnInfo(name = "delay")
    override val delay: Int? = null
): Mark