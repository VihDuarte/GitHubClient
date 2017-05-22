package com.victor.githubclient.extensions

fun Int.formatCount(): String {
    return when (this) {
        0 -> return "0"
        in 1000 until 1000000 -> (this / 1000).toString() + "K"
        in 1000000..Int.MAX_VALUE -> (this / 1000000).toString() + "M"
        else -> this.toString()
    }.replace("^0+".toRegex(), "")
}

