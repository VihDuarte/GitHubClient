package com.victor.githubclient.interactor

import com.victor.githubclient.model.PullRequest
import com.victor.githubclient.model.RepositoriesSearchResult
import com.victor.githubclient.BuildConfig
import java.net.URL
import org.json.JSONObject




fun searchRepositories(filter: String = "language:java", sort: String = "stars", pagination: Int): RepositoriesSearchResult? {
    val url = BuildConfig.URLBASE + "search/repositories?q=" + filter + "&sort=" + sort + "&page=" + pagination
    val json = URL(url).readText()

    val result = RepositoriesSearchResult(JSONObject(json))
    result.pagination = pagination

    return result
}

fun getOfflineRepositories(pagination: Int): RepositoriesSearchResult? {
    return null
}

fun getOffilinePullRequests(creator: String, repository: String): MutableList<PullRequest>? {
    return null
}

fun getAllPullRequests(creator: String, repository: String): MutableList<PullRequest>? {
    return null
}

