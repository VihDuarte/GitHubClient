package com.victor.githubclient.interactor

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.victor.githubclient.extensions.formatToString
import com.victor.githubclient.model.PullRequest
import com.victor.githubclient.model.RepositoriesSearchResult
import com.victor.githubclient.model.Repository
import com.victor.githubclient.model.User

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

fun saveUserInDb(db: SQLiteDatabase, user: User) {
    val userId = user.id

    val cursor = db.rawQuery("SELECT $USER_ID_FIELD " +
            " FROM $USER_TABLE_NAME" +
            " WHERE $USER_ID_FIELD = $userId", null)

    val contentValues: ContentValues = ContentValues()

    contentValues.put(USER_ID_FIELD, user.id)
    contentValues.put(USER_LOGIN_FIELD, user.login)
    contentValues.put(USER_AVATAR_FIELD, user.avatarUrl)
    contentValues.put(USER_NAME_FIELD, user.name)

    if (cursor.count == 0) {
        db.insert(USER_TABLE_NAME, null, contentValues)
    } else {
        db.update(USER_TABLE_NAME, contentValues, "$USER_ID_FIELD = ?", arrayOf(user.id.toString()))
    }
}

fun savePullRequestInDb(db: SQLiteDatabase, pullRequest: PullRequest) {
    if (pullRequest.user != null)
        saveUserInDb(db, pullRequest.user)

    val pullRequestId = pullRequest.id

    val cursor = db.rawQuery("SELECT $PULL_REQUEST_ID_FIELD " +
            " FROM $PULL_REQUEST_TABLE_NAME" +
            " WHERE $PULL_REQUEST_ID_FIELD = $pullRequestId", null)

    val contentValues: ContentValues = ContentValues()

    contentValues.put(PULL_REQUEST_ID_FIELD, pullRequest.id)
    contentValues.put(PULL_REQUEST_CREATOR_FIELD, pullRequest.creator)
    contentValues.put(PULL_REQUEST_REPOSITORY_FIELD, pullRequest.repository)
    contentValues.put(PULL_REQUEST_TITLE_FIELD, pullRequest.title)
    contentValues.put(PULL_REQUEST_BODY_FIELD, pullRequest.body)
    contentValues.put(PULL_REQUEST_HTML_URL_FIELD, pullRequest.htmlUrl)
    contentValues.put(PULL_REQUEST_CREATED_FIELD, pullRequest.createdAt?.formatToString())
    contentValues.put(PULL_REQUEST_ORDER_FIELD, pullRequest.order)
    contentValues.put(PULL_REQUEST_USER_ID_FIELD, pullRequest.user?.id)

    if (cursor.count == 0) {
        db.insert(PULL_REQUEST_TABLE_NAME, null, contentValues)
    } else {
        db.update(PULL_REQUEST_TABLE_NAME, contentValues, "$PULL_REQUEST_ID_FIELD = ?", arrayOf(pullRequest.id.toString()))
    }
}

fun saveRepositoryInDb(db: SQLiteDatabase, pagination: Int, repository: Repository) {
    repository.owner?.let {
        saveUserInDb(db, it)
    }

    val repositoryId = repository.id

    val cursor = db.rawQuery("SELECT $REPOSITORY_ID_FIELD " +
            " FROM $REPOSITORY_TABLE_NAME" +
            " WHERE $REPOSITORY_ID_FIELD = $repositoryId", null)

    val contentValues: ContentValues = ContentValues()

    contentValues.put(REPOSITORY_NAME_FIELD, repository.name)
    contentValues.put(REPOSITORY_ID_FIELD, repository.id)
    contentValues.put(REPOSITORY_DESCRIPTION_FIELD, repository.description)
    contentValues.put(REPOSITORY_STARGAZERS_FIELD, repository.stargazersCount)
    contentValues.put(REPOSITORY_FORKS_FIELD, repository.forksCount)
    contentValues.put(REPOSITORY_LANGUAGE_FIELD, repository.language)
    contentValues.put(REPOSITORY_USER_ID_FIELD, repository.owner?.id)
    contentValues.put(REPOSITORY_PAGINATION_FIELD, pagination)

    if (cursor.count == 0) {
        db.insert(REPOSITORY_TABLE_NAME, null, contentValues)
    } else {
        db.update(REPOSITORY_TABLE_NAME, contentValues, "$REPOSITORY_ID_FIELD = ?", arrayOf(repository.id.toString()))
    }
}

fun saveRepositoriesSearchInDb(db: SQLiteDatabase, repositoriesSearchResult: RepositoriesSearchResult) {
    repositoriesSearchResult.repositories.forEach { saveRepositoryInDb(db, repositoriesSearchResult.pagination, it) }
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

