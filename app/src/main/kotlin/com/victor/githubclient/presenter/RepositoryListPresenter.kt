package com.victor.githubclient.presenter

import android.content.Context
import com.victor.githubclient.interactor.searchRepositories
import com.victor.githubclient.loader.Callback
import com.victor.githubclient.loader.GitHubLoader
import com.victor.githubclient.loader.GitHubLoaderManager
import com.victor.githubclient.model.RepositoriesSearchResult
import com.victor.githubclient.view.RepositoryListView
import android.support.v4.app.LoaderManager


/**
 * Created by Victor on 25/08/2016.
 */
class RepositoryListPresenter(private val view: RepositoryListView) : Callback<RepositoriesSearchResult?> {

    private var firstTime = true
    private var currentTimelinePage = 0


    fun getRepositories(context: Context, loaderManager: LoaderManager) {
        view.showProgress()

        val loader = RepositoryListLoader(context, currentTimelinePage + 1)
        GitHubLoaderManager.init(loaderManager, currentTimelinePage + 1, loader, this)
    }

    override fun onFailure(ex: Exception) {
        view.hideProgress()
        view.showError()
    }

    override fun onSuccess(result: RepositoriesSearchResult?) {
        view.hideProgress()

        if (result != null) {
            currentTimelinePage = result.pagination
            view.showItems(result.repositories)
        } else
            view.showError()
    }

    class RepositoryListLoader(context: Context, private val currentPage: Int) : GitHubLoader<RepositoriesSearchResult?>(context) {
        override fun call(): RepositoriesSearchResult? {
            return searchRepositories(pagination = currentPage)
        }
    }
}
