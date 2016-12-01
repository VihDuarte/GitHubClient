package com.victor.githubclient.interactor

import android.database.sqlite.SQLiteDatabase
import com.victor.githubclient.BuildConfig
import com.victor.githubclient.model.PullRequest
import com.victor.githubclient.model.RepositoriesSearchResult
import com.victor.githubclient.model.Repository
import com.victor.githubclient.model.User
import org.json.JSONObject
import java.net.URL

fun searchRepositories(db: SQLiteDatabase, filter: String = "language:java", sort: String = "stars", pagination: Int): RepositoriesSearchResult? {
    val url = BuildConfig.URLBASE + "search/repositories?q=" + filter + "&sort=" + sort + "&page=" + pagination
    val json = URL(url).readText()

    val result = RepositoriesSearchResult(JSONObject(json))
    result.pagination = pagination

    result.saveInDb(db)
    return result
}

fun getOfflineRepositories(db: SQLiteDatabase, pagination: Int): RepositoriesSearchResult? {
    val result: RepositoriesSearchResult = RepositoriesSearchResult()

    result.offline = true
    result.pagination = pagination

    val repositoryCursor = db.rawQuery("SELECT " +
            "$REPOSITORY_NAME_FIELD, " +
            "$REPOSITORY_DESCRIPTION_FIELD, " +
            "$REPOSITORY_STARGAZERS_FIELD, " +
            "$REPOSITORY_FORKS_FIELD, " +
            "$REPOSITORY_LANGUAGE_FIELD, " +
            "$REPOSITORY_USER_ID_FIELD, " +
            "$REPOSITORY_ID_FIELD" +
            " FROM $REPOSITORY_TABLE_NAME" +
            " WHERE $REPOSITORY_PAGINATION_FIELD = $pagination", null)

    repositoryCursor.moveToFirst()

    loop@ for (i in 0..repositoryCursor.count - 1) {
        val item: Repository = Repository()
        item.name = repositoryCursor.getString(0)
        item.description = repositoryCursor.getString(1)
        item.stargazersCount = repositoryCursor.getInt(2)
        item.forksCount = repositoryCursor.getInt(3)
        item.language = repositoryCursor.getString(4)
        item.id = repositoryCursor.getInt(6)

        val userId = repositoryCursor.getInt(5)

        val userCursor = db.rawQuery("SELECT " +
                "$USER_LOGIN_FIELD," +
                "$USER_AVATAR_FIELD," +
                "$USER_NAME_FIELD," +
                "$USER_ID_FIELD" +
                " FROM $USER_TABLE_NAME" +
                " WHERE $USER_ID_FIELD = $userId", null)

        userCursor.moveToFirst()

        var user: User = User()
        user.login = userCursor.getString(0)
        user.avatarUrl = userCursor.getString(1)
        user.name = userCursor.getString(2)
        user.id = userCursor.getInt(3)

        item.owner = user

        result.repositories.add(item)

        repositoryCursor.moveToNext()
    }

    return result
}

fun getOffilinePullRequests(creator: String, repository: String): MutableList<PullRequest>? {
    return null
}

fun getAllPullRequests(creator: String, repository: String): MutableList<PullRequest>? {
    return null
}
