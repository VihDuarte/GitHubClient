package com.victor.githubclient.interactor

import com.victor.githubclient.model.PullRequest
import com.victor.githubclient.model.RepositoriesSearchResult

fun searchRepositories(filter: String = "language:kotlin", sort: String = "stars", pagination: Int): RepositoriesSearchResult? {
    return null
}

fun getOfflineRepositories(pagination: Int): RepositoriesSearchResult? {
    return null
}

fun getOffilinePullRequests(creator: String, repository: String): MutableList<PullRequest>? {
    return null
}

fun getAllPullRequests(creator: String, repository: String): MutableList<PullRequest>? {
    return null
}

