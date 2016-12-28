package com.victor.githubclient.extensions

fun Int.formatCount(): String {
    val result: String

    if (this == 0) {
        return "0"
    } else if (this > 100000) {
        result = (this / 100000).toString() + "M"
    } else if (this > 1000) {
        result = (this / 1000).toString() + "K"
    } else {
        result = this.toString()
    }

    return result.replace("^0+".toRegex(), "")
}

