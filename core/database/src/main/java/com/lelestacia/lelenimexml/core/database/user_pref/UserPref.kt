package com.lelestacia.lelenimexml.core.database.user_pref

import android.content.Context
import android.provider.Telephony.Carriers.PASSWORD
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.lelestacia.lelenimexml.core.common.Constant.IS_SFW
import com.lelestacia.lelenimexml.core.common.Constant.USER_PREF

class UserPref(mContext: Context) {

    private val masterKey: MasterKey = MasterKey.Builder(mContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .setUserAuthenticationRequired(false)
        .build()

    private val preferences = EncryptedSharedPreferences.create(
        mContext,
        USER_PREF,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun isSafeMode(): Boolean = preferences
        .getBoolean(IS_SFW, true)

    fun switchSafeMode(isSafeMode: Boolean) {
        preferences.edit()
            .putBoolean(IS_SFW, isSafeMode)
            .apply()
    }

    fun getPassword(): String {
        val password = preferences.getString(PASSWORD, "")
        val isNotExist = password.isNullOrEmpty()
        if (isNotExist) {
            val stringChar = ('0'..'z').toList().toTypedArray()
            val newPassword = (1..32).map { stringChar.random() }.joinToString("")
            preferences
                .edit()
                .putString(PASSWORD, newPassword)
                .apply()
            return newPassword
        }
        return preferences.getString(PASSWORD, "") as String
    }
}