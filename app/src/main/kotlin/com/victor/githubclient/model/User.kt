package com.victor.githubclient.model

import org.json.JSONObject

data class User(var id: Int?, var login: String, var avatarUrl: String, var name: String)

fun getUserByJson(json: JSONObject): User {
    var id: Int? = null
    var login = ""
    var avatarUrl = ""
    var name = ""

    if (json.has("id"))
        id = json.getInt("id")

    if (json.has("login"))
        login = json.getString("login")

    if (json.has("avatar_url"))
        avatarUrl = json.getString("avatar_url")

    if (json.has("name"))
        name = json.getString("name")

    return User(id, login, avatarUrl, name);
}


