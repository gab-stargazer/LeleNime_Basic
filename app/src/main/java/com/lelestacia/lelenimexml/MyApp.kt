package com.lelestacia.lelenimexml

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        setDefaultNightMode(MODE_NIGHT_NO)

        /*
         *  The app is currently not supporting dark mode since i don't found the best palette for it yet
         */

        /*val isDarkMode = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
            .getInt(IS_DARK_MODE, 0)
        when (isDarkMode) {
            0 -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            1 -> setDefaultNightMode(MODE_NIGHT_YES)
            2 -> setDefaultNightMode(MODE_NIGHT_NO)
        }*/
    }
}
