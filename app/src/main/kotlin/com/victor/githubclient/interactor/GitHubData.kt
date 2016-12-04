package com.victor.githubclient.interactor

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GitHubData(context: Context) : SQLiteOpenHelper(context, GitHubData.DB_NAME, null, GitHubData.VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $USER_TABLE_NAME ($USER_ID_FIELD INTEGER PRIMARY KEY," +
                "$USER_LOGIN_FIELD TEXT," +
                "$USER_AVATAR_FIELD TEXT," +
                "$USER_NAME_FIELD TEXT);")

        db.execSQL("CREATE TABLE $REPOSITORY_TABLE_NAME ($REPOSITORY_ID_FIELD INTEGER PRIMARY KEY," +
                "$REPOSITORY_NAME_FIELD TEXT," +
                "$REPOSITORY_DESCRIPTION_FIELD TEXT," +
                "$REPOSITORY_STARGAZERS_FIELD INTEGER," +
                "$REPOSITORY_FORKS_FIELD INTEGER," +
                "$REPOSITORY_LANGUAGE_FIELD TEXT," +
                "$REPOSITORY_USER_ID_FIELD INTEGER," +
                "$REPOSITORY_PAGINATION_FIELD INTEGER," +
                "FOREIGN KEY($REPOSITORY_USER_ID_FIELD) REFERENCES $USER_TABLE_NAME($USER_ID_FIELD));")

        db.execSQL("CREATE TABLE $PULL_REQUEST_TABLE_NAME ($PULL_REQUEST_ID_FIELD INTEGER PRIMARY KEY," +
                "$PULL_REQUEST_CREATOR_FIELD TEXT," +
                "$PULL_REQUEST_REPOSITORY_FIELD TEXT," +
                "$PULL_REQUEST_TITLE_FIELD TEXT," +
                "$PULL_REQUEST_BODY_FIELD TEXT," +
                "$PULL_REQUEST_CREATED_FIELD DATE," +
                "$PULL_REQUEST_HTML_URL_FIELD TEXT," +
                "$PULL_REQUEST_USER_ID_FIELD INTEGER," +
                "$PULL_REQUEST_ORDER_FIELD INTEGER," +
                "FOREIGN KEY($PULL_REQUEST_USER_ID_FIELD) REFERENCES $USER_TABLE_NAME($USER_ID_FIELD));")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
    }

    companion object {
        private val DB_NAME = "GitHubClient"
        private val VERSION = 1
    }
}

val USER_TABLE_NAME = "User"
val USER_LOGIN_FIELD = "login"
val USER_AVATAR_FIELD = "avatar_url"
val USER_NAME_FIELD = "name"
val USER_ID_FIELD = "_id"

val REPOSITORY_TABLE_NAME = "Repository"
val REPOSITORY_NAME_FIELD = "name"
val REPOSITORY_DESCRIPTION_FIELD = "description"
val REPOSITORY_STARGAZERS_FIELD = "stargazers_count"
val REPOSITORY_FORKS_FIELD = "forks_count"
val REPOSITORY_LANGUAGE_FIELD = "language"
val REPOSITORY_PAGINATION_FIELD = "pagination"
val REPOSITORY_USER_ID_FIELD = "user_id"
val REPOSITORY_ID_FIELD = "_id"

val PULL_REQUEST_TABLE_NAME = "PullResquest"
val PULL_REQUEST_CREATOR_FIELD = "creator"
val PULL_REQUEST_REPOSITORY_FIELD = "repository"
val PULL_REQUEST_TITLE_FIELD = "title"
val PULL_REQUEST_BODY_FIELD = "body"
val PULL_REQUEST_CREATED_FIELD = "createdAt"
val PULL_REQUEST_HTML_URL_FIELD = "htmlUrl"
val PULL_REQUEST_USER_ID_FIELD = "user_id"
val PULL_REQUEST_ORDER_FIELD = "order_view"
val PULL_REQUEST_ID_FIELD = "_id"

