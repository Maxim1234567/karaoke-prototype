package ru.araok.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MarkDb::class,
        SettingsDb::class,
        SettingsMarksDb::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
}