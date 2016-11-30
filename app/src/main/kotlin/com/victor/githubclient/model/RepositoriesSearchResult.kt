package com.victor.githubclient.model

import org.json.JSONObject

class RepositoriesSearchResult {

    constructor(json: JSONObject) {
        var items = json.getJSONArray("items")
        (0..(items.length() - 1)).mapTo(repositories) {
            Repository(items.getJSONObject(it))
        }
    }

    var pagination: Int = 0

    val repositories: MutableList<Repository> = arrayListOf()

}

