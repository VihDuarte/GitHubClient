package com.victor.githubclient.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatToString(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)
}