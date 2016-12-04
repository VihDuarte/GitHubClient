package com.victor.githubclient.interactor

import android.database.sqlite.SQLiteDatabase
import com.victor.githubclient.BuildConfig
import com.victor.githubclient.model.PullRequest
import com.victor.githubclient.model.RepositoriesSearchResult
import com.victor.githubclient.model.Repository
import com.victor.githubclient.model.User
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

fun searchRepositories(db: SQLiteDatabase, filter: String = "language:java", sort: String = "stars", pagination: Int): RepositoriesSearchResult? {
    val url = BuildConfig.URLBASE + "search/repositories?q=$filter&sort=$sort&page=$pagination"
    val json = URL(url).readText()

    val result = RepositoriesSearchResult(JSONObject(json))
    result.pagination = pagination

    result.saveInDb(db)
    return result
}

fun getOfflineRepositories(db: SQLiteDatabase, pagination: Int): RepositoriesSearchResult? {
    val result: RepositoriesSearchResult = RepositoriesSearchResult()
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

fun getAllPullRequests(db: SQLiteDatabase, creator: String, repository: String): MutableList<PullRequest> {
    val url = BuildConfig.URLBASE + "repos/$creator/$repository/pulls"
    val json = URL(url).readText()

    val allPullRequest = JSONArray(json)

    val result: MutableList<PullRequest> = arrayListOf()

    loop@ for (i in 0..allPullRequest.length() - 1) {
        val item = PullRequest(allPullRequest.getJSONObject(i))

        item.creator = creator
        item.repository = repository
        item.order = i

        result.add(item)
        item.saveInDb(db)
    }

    return result
}

fun getOffilinePullRequests(db: SQLiteDatabase, creator: String, repository: String): MutableList<PullRequest> {
    val pullRequestCursor = db.rawQuery("SELECT " +
            "$PULL_REQUEST_CREATOR_FIELD, " +
            "$PULL_REQUEST_REPOSITORY_FIELD, " +
            "$PULL_REQUEST_TITLE_FIELD, " +
            "$PULL_REQUEST_BODY_FIELD, " +
            "$PULL_REQUEST_CREATED_FIELD, " +
            "$PULL_REQUEST_HTML_URL_FIELD," +
            "$PULL_REQUEST_USER_ID_FIELD," +
            "$PULL_REQUEST_ORDER_FIELD," +
            "$PULL_REQUEST_ID_FIELD" +
            " FROM $PULL_REQUEST_TABLE_NAME" +
            " WHERE $PULL_REQUEST_CREATOR_FIELD = '$creator'" +
            " AND $PULL_REQUEST_REPOSITORY_FIELD = '$repository'" +
            " ORDER BY $PULL_REQUEST_ORDER_FIELD ASC", null)

    pullRequestCursor.moveToFirst()

    val result: MutableList<PullRequest> = arrayListOf()
    loop@ for (i in 0..pullRequestCursor.count - 1) {
        var item = PullRequest()

        item.creator = pullRequestCursor.getString(0)
        item.repository = pullRequestCursor.getString(1)
        item.title = pullRequestCursor.getString(2)
        item.body = pullRequestCursor.getString(3)
        item.createdAt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(pullRequestCursor.getString(4))
        item.htmlUrl = pullRequestCursor.getString(5)
        item.id = pullRequestCursor.getInt(8)

        val userId = pullRequestCursor.getInt(6)

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

        item.user = user

        result.add(item)

        pullRequestCursor.moveToNext()
    }

    return result
}
