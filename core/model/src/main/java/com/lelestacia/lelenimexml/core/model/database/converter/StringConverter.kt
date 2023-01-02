package com.lelestacia.lelenimexml.core.model.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringConverter {

    @TypeConverter
    fun stringToList(stringParam: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        if (stringParam.isEmpty())
            return emptyList()
        return Gson().fromJson(stringParam, type)
    }

    @TypeConverter
    fun listToString(listParam: List<String>): String {
        val type = object : TypeToken<List<String>>() {}.type
        if (listParam.isEmpty())
            return ""
        return Gson().toJson(listParam, type)
    }
}
