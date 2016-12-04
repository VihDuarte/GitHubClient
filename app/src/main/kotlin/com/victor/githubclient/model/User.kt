package com.victor.githubclient.model

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.victor.githubclient.interactor.*
import org.json.JSONObject

class User {
    constructor()
    constructor(json: JSONObject) {
        if (json.has("id"))
            id = json.getInt("id")

        if (json.has("login"))
            login = json.getString("login")

        if (json.has("avatar_url"))
            avatarUrl = json.getString("avatar_url")

        if (json.has("name"))
            name = json.getString("name")
    }

    var id: Int? = null

    var login: String? = null

    var avatarUrl: String? = null

    var name: String? = null

    fun saveInDb(db: SQLiteDatabase) {
        val cursor = db.rawQuery("SELECT $USER_ID_FIELD " +
                " FROM $USER_TABLE_NAME" +
                " WHERE $USER_ID_FIELD = $id", null)

        val contentValues: ContentValues = ContentValues()

        contentValues.put(USER_ID_FIELD, id)
        contentValues.put(USER_LOGIN_FIELD, login)
        contentValues.put(USER_AVATAR_FIELD, avatarUrl)
        contentValues.put(USER_NAME_FIELD, name)

        if (cursor.count == 0) {
            db.insert(USER_TABLE_NAME, null, contentValues)
        } else {
            db.update(USER_TABLE_NAME, contentValues, "$USER_ID_FIELD = ?", arrayOf(id.toString()))
        }
    }
}
