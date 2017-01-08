package com.victor.githubclient.interactor

import android.database.sqlite.SQLiteDatabase
import com.victor.githubclient.BuildConfig
import com.victor.githubclient.model.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

fun searchRepositories(db: SQLiteDatabase, filter: String = "language:java", sort: String = "stars", pagination: Int): RepositoriesSearchResult? {
    val url = BuildConfig.URLBASE + "search/repositories?q=$filter&sort=$sort&page=$pagination"
    val json = URL(url).readText()

    val result = getRepositoriesSearchByJson(JSONObject(json))
    result.pagination = pagination

    saveRepositoriesSearchInDb(db, result)
    return result
}

fun getOfflineRepositories(db: SQLiteDatabase, pagination: Int): RepositoriesSearchResult? {

    val repositories: MutableList<Repository> = arrayListOf()

    val repositoryCursor = db.rawQuery("SELECT " +
            "$REPOSITORY_NAME_FIELD, " +
            "$REPOSITORY_DESCRIPTION_FIELD, " +
            "$REPOSITORY_STARGAZERS_FIELD, " +
            "$REPOSITORY_FORKS_FIELD, " +
            "$REPOSITORY_LANGUAGE_FIELD, " +
            "$REPOSITORY_USER_ID_FIELD, " +
            "$REPOSITORY_ID_FIELD" +
            " FROM $REPOSITORY_TABLE_NAME" +
            " WHERE $REPOSITORY_PAGINATION_FIELD = $pagination" +
            " ORDER BY $REPOSITORY_STARGAZERS_FIELD DESC", null)

    repositoryCursor.moveToFirst()

    loop@ for (i in 0..repositoryCursor.count - 1) {

        val userId = repositoryCursor.getInt(5)

        val userCursor = db.rawQuery("SELECT " +
                "$USER_LOGIN_FIELD," +
                "$USER_AVATAR_FIELD," +
                "$USER_NAME_FIELD," +
                "$USER_ID_FIELD" +
                " FROM $USER_TABLE_NAME" +
                " WHERE $USER_ID_FIELD = $userId", null)

        userCursor.moveToFirst()

        var user = User(userCursor.getInt(3),
                userCursor.getString(0),
                userCursor.getString(1),
                userCursor.getString(2))

        val item: Repository = Repository(repositoryCursor.getInt(6),
                repositoryCursor.getString(0),
                user,
                repositoryCursor.getString(1),
                repositoryCursor.getInt(2),
                repositoryCursor.getInt(3),
                repositoryCursor.getString(4))

        repositories.add(item)

        repositoryCursor.moveToNext()
    }

    val result: RepositoriesSearchResult = RepositoriesSearchResult(pagination, repositories)

    return result
}

fun getAllPullRequests(db: SQLiteDatabase, creator: String, repository: String): MutableList<PullRequest> {
    val url = BuildConfig.URLBASE + "repos/$creator/$repository/pulls"
    val json = URL(url).readText()

    val allPullRequest = JSONArray(json)

    val result: MutableList<PullRequest> = arrayListOf()

    loop@ for (i in 0..allPullRequest.length() - 1) {
        var jsonObject = allPullRequest.getJSONObject(i)
        jsonObject.put("creator", creator)
        jsonObject.put("repository", repository)
        jsonObject.put("order", i)

        val item = getPullRequestByJson(jsonObject)

        result.add(item)
        savePullRequestInDb(db, item)
    }

    return result
}

fun getOfflinePullRequests(db: SQLiteDatabase, creator: String, repository: String): MutableList<PullRequest> {
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

        val userId = pullRequestCursor.getInt(6)

        val userCursor = db.rawQuery("SELECT " +
                "$USER_LOGIN_FIELD," +
                "$USER_AVATAR_FIELD," +
                "$USER_NAME_FIELD," +
                "$USER_ID_FIELD" +
                " FROM $USER_TABLE_NAME" +
                " WHERE $USER_ID_FIELD = $userId", null)

        userCursor.moveToFirst()

        var user = User(userCursor.getInt(3),
                userCursor.getString(0),
                userCursor.getString(1),
                userCursor.getString(2))

        var item = PullRequest(pullRequestCursor.getInt(8),
                pullRequestCursor.getString(0),
                pullRequestCursor.getString(1),
                user,
                pullRequestCursor.getString(2),
                pullRequestCursor.getString(3),
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(pullRequestCursor.getString(4)),
                pullRequestCursor.getString(5),
                pullRequestCursor.getInt(7))

        result.add(item)

        pullRequestCursor.moveToNext()
    }

    return result
}
