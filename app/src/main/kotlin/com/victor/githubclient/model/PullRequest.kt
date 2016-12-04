package com.victor.githubclient.model

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.victor.githubclient.interactor.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class PullRequest {
    constructor()
    constructor(json: JSONObject) {
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
            user = User(json.getJSONObject("user"))

        if (json.has("html_url"))
            htmlUrl = json.getString("html_url")

    }

    var id: Int = 0

    var creator: String = ""

    var repository: String = ""

    var user: User? = null

    var title: String = ""

    var body: String = ""

    var createdAt: Date? = null

    var htmlUrl: String = ""

    var order: Int? = null

    var createdAtFormated: String = ""
        get() {
            return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(createdAt)
        }


    fun saveInDb(db: SQLiteDatabase) {
        user?.saveInDb(db)

        val cursor = db.rawQuery("SELECT $PULL_REQUEST_ID_FIELD " +
                " FROM $PULL_REQUEST_TABLE_NAME" +
                " WHERE $PULL_REQUEST_ID_FIELD = $id", null)

        val contentValues: ContentValues = ContentValues()

        contentValues.put(PULL_REQUEST_ID_FIELD, id)
        contentValues.put(PULL_REQUEST_CREATOR_FIELD, creator)
        contentValues.put(PULL_REQUEST_REPOSITORY_FIELD, repository)
        contentValues.put(PULL_REQUEST_TITLE_FIELD, title)
        contentValues.put(PULL_REQUEST_BODY_FIELD, body)
        contentValues.put(PULL_REQUEST_HTML_URL_FIELD, htmlUrl)
        contentValues.put(PULL_REQUEST_CREATED_FIELD, createdAtFormated)
        contentValues.put(PULL_REQUEST_ORDER_FIELD, order)
        contentValues.put(PULL_REQUEST_USER_ID_FIELD, user?.id)

        if (cursor.count == 0) {
            db.insert(PULL_REQUEST_TABLE_NAME, null, contentValues)
        } else {
            db.update(PULL_REQUEST_TABLE_NAME, contentValues, "$PULL_REQUEST_ID_FIELD = ?", arrayOf(id.toString()))
        }
    }
}