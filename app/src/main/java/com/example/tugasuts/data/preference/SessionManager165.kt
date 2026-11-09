package com.example.tugasuts.data.preference

import android.content.Context
import android.content.SharedPreferences

class SessionManager165(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME_165, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME_165 = "expense_tracker_session_165"
        private const val KEY_USER_ID_165 = "currentUserId165"
        private const val KEY_USERNAME_165 = "currentUsername165"
        private const val KEY_IS_LOGGED_IN_165 = "isLoggedIn165"
    }

    fun saveUserSession165(userId: Long, username: String) {
        prefs.edit().apply {
            putLong(KEY_USER_ID_165, userId)
            putString(KEY_USERNAME_165, username)
            putBoolean(KEY_IS_LOGGED_IN_165, true)
            apply()
        }
    }

    fun getCurrentUserId165(): Long {
        return prefs.getLong(KEY_USER_ID_165, -1L)
    }

    fun getCurrentUsername165(): String? {
        return prefs.getString(KEY_USERNAME_165, null)
    }

    fun isLoggedIn165(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN_165, false)
    }

    fun clearSession165() {
        prefs.edit().clear().apply()
    }
}
