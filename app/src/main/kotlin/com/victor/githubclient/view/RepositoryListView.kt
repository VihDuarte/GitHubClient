package com.victor.githubclient.view

import com.victor.githubclient.model.Repository

interface RepositoryListView : BaseView {
    fun showItems(items: MutableList<Repository>)
}
