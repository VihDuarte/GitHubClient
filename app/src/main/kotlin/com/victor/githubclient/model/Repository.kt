package com.victor.githubclient.model

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.victor.githubclient.interactor.*
import org.json.JSONObject

class Repository {
    constructor()
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

    fun saveInDb(db: SQLiteDatabase, pagination: Int) {
        owner?.saveInDb(db)

        val cursor = db.rawQuery("SELECT $REPOSITORY_ID_FIELD " +
                " FROM $REPOSITORY_TABLE_NAME" +
                " WHERE $REPOSITORY_ID_FIELD = $id", null)

        if (cursor.count == 0) {
            val contentValues: ContentValues = ContentValues()

            contentValues.put(REPOSITORY_NAME_FIELD, name)
            contentValues.put(REPOSITORY_ID_FIELD, id)
            contentValues.put(REPOSITORY_DESCRIPTION_FIELD, description)
            contentValues.put(REPOSITORY_STARGAZERS_FIELD, stargazersCount)
            contentValues.put(REPOSITORY_FORKS_FIELD, forksCount)
            contentValues.put(REPOSITORY_LANGUAGE_FIELD, language)
            contentValues.put(REPOSITORY_USER_ID_FIELD, owner?.id)
            contentValues.put(REPOSITORY_PAGINATION_FIELD, pagination)

            db.insert(REPOSITORY_TABLE_NAME, null, contentValues)
        }
    }
}
