package com.victor.githubclient.model

import org.json.JSONObject

data class User(val id: Int?, val login: String, val avatarUrl: String, val name: String)

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


