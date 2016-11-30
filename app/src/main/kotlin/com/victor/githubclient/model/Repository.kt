package com.victor.githubclient.model

import org.json.JSONObject

class Repository {
    constructor(json: JSONObject) {
        if (json.has("id"))
            id = json.getInt("id")

        if (json.has("name"))
            name = json.getString("name")

        if (json.has("description"))
            description = json.getString("description")

        if (json.has("stargazers_count"))
            stargazersCount = json.getInt("stargazers_count")

        if (json.has("forks_count"))
            forksCount = json.getInt("forks_count")

        if (json.has("language"))
            language = json.getString("language")

        if (json.has("owner"))
            owner = User(json.getJSONObject("owner"))
    }

    var id: Int? = null

    var name: String? = null

    var owner: User? = null

    var description: String? = null

    var stargazersCount: Int? = null

    var forksCount: Int? = null

    var language: String? = null
}
