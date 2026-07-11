package com.vlg.teachgame.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("TeachGame", Context.MODE_PRIVATE)

    fun saveState(i: Boolean) {
        prefs.edit { putBoolean("sate", i) }
    }

    fun isHomework(): Boolean {
        return prefs.getBoolean("state", true)
    }

}