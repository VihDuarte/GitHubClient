package com.victor.githubclient.interactor

import com.victor.githubclient.model.PullRequest
import com.victor.githubclient.model.RepositoriesSearchResult

/**
 * Created by Victor on 25/08/2016.
 */
class ApiInteractor {

    fun searchRepositories(filter: String, sort: String, pagination: Int): RepositoriesSearchResult? {
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
}
