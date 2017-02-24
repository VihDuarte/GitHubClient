package com.victor.githubclient.view

import com.victor.githubclient.model.PullRequest

interface RepositoryDetailView : BaseView {
    fun showProgress()

    fun hideProgress()

    fun showItems(items: MutableList<PullRequest>)
}
