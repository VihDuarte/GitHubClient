package com.victor.githubclient.utils

fun formatCount(count: Int): String {
    val result: String

    if (count == 0) {
        return "0"
    } else if (count > 100000) {
        result = (count / 100000).toString() + "M"
    } else if (count > 1000) {
        result = (count / 1000).toString() + "K"
    } else {
        result = count.toString()
    }

    return result.replace("^0+".toRegex(), "")
}

