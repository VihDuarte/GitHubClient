package com.victor.githubclient.model

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class PullRequest(var id: Int?,
                       var creator: String,
                       var repository: String,
                       var user: User?,
                       var title: String,
                       var body: String,
                       var createdAt: Date?,
                       var htmlUrl: String,
                       var order: Int?)

fun getCreatedAtFormated(date: Date?): String{
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
}

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