package ru.araok.data

import android.content.Context
import android.content.Context.MODE_PRIVATE

private const val PREFERENCE_NAME = "store"
private const val ACCESS_TOKEN = "accessToken"
private const val REFRESH_TOKEN = "refreshToken"
private const val USER_ID = "userId"

object Repository {
    private var localAccessToken: String? = null
    private var localRefreshToken: String? = null
    private var localUserId: Long? = null

    fun getAccessToken(context: Context): String {
        return localAccessToken ?: getAccessTokenFromSharedPreference(context) ?: ""
    }

    fun getRefreshToken(context: Context): String {
        return localRefreshToken ?: getRefreshTokenFromSharedPreference(context) ?: ""
    }

    fun getUserId(context: Context): Long {
        return localUserId ?: getUserIdFromSharedPreference(context)
    }

    fun saveAccessToken(context: Context, accessToken: String) {
        localAccessToken = accessToken

        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putString(ACCESS_TOKEN, accessToken)
        edit.apply()
    }

    fun saveRefreshToken(context: Context, refreshToken: String) {
        localRefreshToken = refreshToken

        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putString(REFRESH_TOKEN, refreshToken)
        edit.apply()
    }

    fun saveUserId(context: Context, userId: Long) {
        localUserId = userId

        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putLong(USER_ID, userId)
        edit.apply()
    }

    private fun getAccessTokenFromSharedPreference(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        return prefs.getString(ACCESS_TOKEN, null)
    }

    private fun getRefreshTokenFromSharedPreference(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        return prefs.getString(REFRESH_TOKEN, null)
    }

    private fun getUserIdFromSharedPreference(context: Context): Long {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        return prefs.getLong(USER_ID, 0)
    }
}