package ru.araok

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

private lateinit var app: App

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        app = this
        super.onCreate()
    }

    companion object {
        fun getContext(): Context = app
    }
}