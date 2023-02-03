package com.lelestacia.lelenimexml

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate.*
import com.lelestacia.lelenimexml.core.common.Constant.IS_DARK_MODE
import com.lelestacia.lelenimexml.core.common.Constant.USER_PREF
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        val isDarkMode = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
            .getInt(IS_DARK_MODE, 0)
        when (isDarkMode) {
            0 -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            1 -> setDefaultNightMode(MODE_NIGHT_YES)
            2 -> setDefaultNightMode(MODE_NIGHT_NO)
        }
    }
}
