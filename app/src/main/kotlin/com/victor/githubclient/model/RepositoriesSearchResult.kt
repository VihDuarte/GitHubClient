package com.victor.githubclient.model

import org.json.JSONObject

data class RepositoriesSearchResult(var pagination: Int, val repositories: MutableList<Repository>)

fun getRepositoriesSearchByJson(json: JSONObject): RepositoriesSearchResult {
    val repositories: MutableList<Repository> = arrayListOf()

    var items = json.getJSONArray("items")
    (0..(items.length() - 1)).mapTo(repositories) {
        getRepositoryByJson(items.getJSONObject(it))
    }

    return RepositoriesSearchResult(0, repositories)
}


