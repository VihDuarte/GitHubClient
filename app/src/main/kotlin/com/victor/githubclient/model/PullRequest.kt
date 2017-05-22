package com.victor.githubclient.model

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class PullRequest(val id: Int?,
                       val creator: String,
                       val repository: String,
                       val user: User?,
                       val title: String,
                       val body: String,
                       val createdAt: Date?,
                       val htmlUrl: String,
                       val order: Int?)


fun getPullRequestByJson(json: JSONObject): PullRequest {
    var id: Int? = null
    var title = ""
    var body = ""
    var createdAt: Date? = null
    var user: User? = null
    var htmlUrl = ""
    var creator = ""
    var repository = ""
    var order: Int? = null

    if (json.has("id"))
        id = json.getInt("id")

    if (json.has("title"))
        title = json.getString("title")

    if (json.has("body"))
        body = json.getString("body")

    if (json.has("created_at")) {
        val date = json.getString("created_at")
        createdAt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
    }

    if (json.has("user"))
        user = getUserByJson(json.getJSONObject("user"))

    if (json.has("html_url"))
        htmlUrl = json.getString("html_url")

    if (json.has("creator"))
        creator = json.getString("creator")

    if (json.has("repository"))
        repository = json.getString("repository")

    if (json.has("order"))
        order = json.getInt("order")

    return PullRequest(id, creator, repository, user, title, body, createdAt, htmlUrl, order)
}