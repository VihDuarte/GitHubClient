package com.victor.githubclient.model

import android.database.sqlite.SQLiteDatabase
import org.json.JSONObject

class RepositoriesSearchResult {
    constructor()
    constructor(json: JSONObject) {
        var items = json.getJSONArray("items")
        (0..(items.length() - 1)).mapTo(repositories) {
            Repository(items.getJSONObject(it))
        }
    }

    var offline: Boolean = false

    var pagination: Int = 0

    val repositories: MutableList<Repository> = arrayListOf()


    fun saveInDb(db: SQLiteDatabase) {
        repositories.forEach { it.saveInDb(db, pagination) }
    }
}

