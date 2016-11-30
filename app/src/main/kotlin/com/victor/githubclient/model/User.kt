package com.victor.githubclient.model

import org.json.JSONObject


class User {

    constructor(json: JSONObject) {
        if (json.has("id"))
            id = json.getInt("id")

        if (json.has("login"))
            login = json.getString("login")

        if (json.has("avatar_url"))
            avatarUrl = json.getString("avatar_url")

        if (json.has("name"))
            name = json.getString("name")
    }

    var id: Int? = null

    var login: String? = null

    var avatarUrl: String? = null

    var name: String? = null
}
