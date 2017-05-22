package com.victor.githubclient.model

import org.json.JSONObject

data class Repository( val id: Int?,
                      val name: String,
                      val owner: User?,
                      val description: String,
                      val stargazersCount: Int?,
                      val forksCount: Int?,
                      val language: String)

fun getRepositoryByJson(json: JSONObject): Repository {
    var id: Int? = null
    var name = ""
    var description = ""
    var stargazersCount: Int? = null
    var forksCount: Int? = null
    var language = ""
    var owner: User? = null

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
        owner = getUserByJson(json.getJSONObject("owner"))

    return Repository(id, name, owner, description, stargazersCount, forksCount, language)
}


