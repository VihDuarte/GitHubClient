package com.victor.githubclient.view

import com.victor.githubclient.model.PullRequest


interface RepositoryDetailView : BaseView {
    fun showItems(items: MutableList<PullRequest>)
}
