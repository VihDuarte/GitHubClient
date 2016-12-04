package com.victor.githubclient.view

import com.victor.githubclient.model.Repository

interface RepositoryListView  {

    fun showProgress()

    fun hideProgress()

    fun showError()

    fun hideError()

    fun cleanData()

    fun showItems(items: MutableList<Repository>)
}
