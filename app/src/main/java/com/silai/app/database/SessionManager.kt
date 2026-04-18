package com.silai.app.database

import android.content.Context
import android.content.SharedPreferences

/**
 * SessionManager.kt
 * ==================
 * Manages user login session using SharedPreferences.
 * SharedPreferences = simple key-value storage (like a small config file).
 *
 * VIVA TIP: SharedPreferences is used to remember small data like login state
 * between app restarts. Unlike SQLite, it's NOT for large/structured data.
 *
 * We store: isLoggedIn, userId, username
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "SilaiAppSession"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USERNAME = "username"
    }

    // Save login info to SharedPreferences
    fun saveLoginSession(userId: Int, username: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            apply()
        }
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    // Get saved user ID
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)

    // Get saved username
    fun getUsername(): String = prefs.getString(KEY_USERNAME, "") ?: ""

    // Clear session on logout
    fun logout() {
        prefs.edit().clear().apply()
    }
}