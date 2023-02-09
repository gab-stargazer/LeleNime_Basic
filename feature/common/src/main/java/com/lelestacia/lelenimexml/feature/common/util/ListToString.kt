package com.lelestacia.lelenimexml.feature.common.util

class ListToString {
    operator fun invoke(list: List<String>): String {
        var newList = ""
        for (i in list.indices) {
            newList +=
                if (i == 0) list[i]
                else ", ${list[i]}"
        }
        return newList
    }
}
