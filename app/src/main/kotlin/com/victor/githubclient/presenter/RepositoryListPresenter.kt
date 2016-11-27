package com.victor.githubclient.presenter

import android.content.Context
import com.victor.githubclient.interactor.ApiInteractor
import com.victor.githubclient.view.RepositoryListView


/**
 * Created by Victor on 25/08/2016.
 */
class RepositoryListPresenter(private val view: RepositoryListView, private val interactor: ApiInteractor) {
    private var currentTimelinePage = 0

    private var firstTime = true

    fun getRepositories(context: Context) {
        view.showProgress()
    }
}
