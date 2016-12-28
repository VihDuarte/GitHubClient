package com.victor.githubclient.presenter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.v4.app.LoaderManager
import com.victor.githubclient.extensions.isOnline
import com.victor.githubclient.interactor.GitHubData
import com.victor.githubclient.interactor.getAllPullRequests
import com.victor.githubclient.interactor.getOffilinePullRequests
import com.victor.githubclient.loader.Callback
import com.victor.githubclient.loader.GitHubLoader
import com.victor.githubclient.loader.GitHubLoaderManager
import com.victor.githubclient.model.PullRequest
import com.victor.githubclient.view.RepositoryDetailView


class RepositoryDetailPresenter() {
    private var firstTime = true
    private var view: RepositoryDetailView? = null
    private var githubData: GitHubData? = null
    private var context: Context? = null

    fun attachView(context: Context, view: RepositoryDetailView) {
        this.view = view
        this.context = context
        githubData = GitHubData(context)
    }

    fun detachView() {
        this.view = null
        githubData?.close()
    }

    fun getPullRequest(loaderManager: LoaderManager, creator: String, repository: String) {
        if (view == null || context == null)
            throw Exception("attach view to use the presenter")

        view?.showProgress()

        if (firstTime || !context!!.isOnline()) {
            val loaderOffline = RepositoryDetailLoaderOffline(context!!, creator, repository, githubData!!.readableDatabase)
            GitHubLoaderManager.init(loaderManager, -1, loaderOffline, (object : Callback<MutableList<PullRequest>> {
                override fun onFailure(ex: Exception) {
                    //do nothing
                }

                override fun onSuccess(result: MutableList<PullRequest>) {
                    view?.showItems(result)
                }

            }))
        }

        val loader = RepositoryDetailLoader(context!!, creator, repository, githubData!!.readableDatabase)
        GitHubLoaderManager.init(loaderManager, 1, loader, (object : Callback<MutableList<PullRequest>> {
            override fun onFailure(ex: Exception) {
                view?.hideProgress()
                view?.showError()
            }

            override fun onSuccess(result: MutableList<PullRequest>) {
                if (firstTime) {
                    view?.cleanData()
                    firstTime = false
                }

                view?.showItems(result)
                view?.hideError()
                view?.hideProgress()
            }

        }))
    }

    class RepositoryDetailLoader(context: Context, private val creator: String, private val repository: String, private val sqLiteDatabase: SQLiteDatabase) : GitHubLoader<MutableList<PullRequest>>(context) {
        override fun call(): MutableList<PullRequest> {
            return getAllPullRequests(db = sqLiteDatabase, creator = creator, repository = repository)
        }
    }

    class RepositoryDetailLoaderOffline(context: Context, private val creator: String, private val repository: String, private val sqLiteDatabase: SQLiteDatabase) : GitHubLoader<MutableList<PullRequest>>(context) {
        override fun call(): MutableList<PullRequest> {
            return getOffilinePullRequests(db = sqLiteDatabase, creator = creator, repository = repository)
        }
    }
}
