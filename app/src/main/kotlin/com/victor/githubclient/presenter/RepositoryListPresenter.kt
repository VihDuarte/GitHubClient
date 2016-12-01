package com.victor.githubclient.presenter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.victor.githubclient.interactor.searchRepositories
import com.victor.githubclient.loader.Callback
import com.victor.githubclient.loader.GitHubLoader
import com.victor.githubclient.loader.GitHubLoaderManager
import com.victor.githubclient.model.RepositoriesSearchResult
import com.victor.githubclient.view.RepositoryListView
import android.support.v4.app.LoaderManager
import com.victor.githubclient.interactor.GitHubData
import com.victor.githubclient.interactor.getOfflineRepositories
import com.victor.githubclient.utils.isOnline


/**
 * Created by Victor on 25/08/2016.
 */
class RepositoryListPresenter : Callback<RepositoriesSearchResult?> {
    private var firstTime = true
    private var currentTimelinePage = 0
    private var view: RepositoryListView? = null
    private var githubData: GitHubData? = null
    private var context: Context? = null

    fun getRepositories(loaderManager: LoaderManager) {
        if(view == null || context == null)
            throw Exception("attach view to use the presenter")

        view?.showProgress()

        if (firstTime || !isOnline(context!!)) {
            val loaderOffiline = RepositoryListLoaderOffline(context!!, currentTimelinePage + 1, githubData!!.readableDatabase)
            GitHubLoaderManager.init(loaderManager, -(currentTimelinePage + 1), loaderOffiline, this)
        }

        val loader = RepositoryListLoader(context!!, currentTimelinePage + 1, githubData!!.writableDatabase)
        GitHubLoaderManager.init(loaderManager, currentTimelinePage + 1, loader, this)
    }

    fun attachView(context: Context, view: RepositoryListView) {
        this.view = view
        this.context = context
        githubData = GitHubData(context)
    }

    fun dettachView() {
        this.view = null
        githubData?.close()
    }

    override fun onFailure(ex: Exception) {
        view?.hideProgress()
        view?.showError()
    }

    override fun onSuccess(result: RepositoriesSearchResult?) {
        view?.hideProgress()

        if (result != null) {
            currentTimelinePage = result.pagination

            if (firstTime && result.offline) {
                view?.cleanData()
                firstTime = false
            }

            view?.showItems(result.repositories)
            view?.hideError()
        } else
            view?.showError()
    }

    class RepositoryListLoader(context: Context, private val currentPage: Int, private val sqLiteDatabase: SQLiteDatabase) : GitHubLoader<RepositoriesSearchResult?>(context) {
        override fun call(): RepositoriesSearchResult? {
            return searchRepositories(pagination = currentPage, db = sqLiteDatabase)
        }
    }

    class RepositoryListLoaderOffline(context: Context, private val currentPage: Int, private val sqLiteDatabase: SQLiteDatabase) : GitHubLoader<RepositoriesSearchResult?>(context) {
        override fun call(): RepositoriesSearchResult? {
            return getOfflineRepositories(pagination = currentPage, db = sqLiteDatabase)
        }
    }
}
