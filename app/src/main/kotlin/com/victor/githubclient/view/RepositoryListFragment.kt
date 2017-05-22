package com.victor.githubclient.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victor.githubclient.R
import com.victor.githubclient.model.Repository
import com.victor.githubclient.presenter.RepositoryListPresenter
import kotlinx.android.synthetic.main.fragment_repository_list.*

class RepositoryListFragment : Fragment(), RepositoryListView {
    lateinit var repositoryListAdapter: RepositoryListAdapter
    lateinit var snackbar: Snackbar
    lateinit var presenter: RepositoryListPresenter
    var repositoryList: MutableList<Repository> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_repository_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true

        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showItems(items: MutableList<Repository>) {
        repositoryList.addAll(items)
        repositoryListAdapter.notifyDataSetChanged()
    }

    override fun cleanData() {
        repositoryList.clear()
    }

    override fun showError() {
        snackbar.show()
    }

    override fun hideError() {
        if (snackbar.isShown) {
            snackbar.dismiss()
        }
    }

    fun init() {
        presenter = RepositoryListPresenter()
        presenter.attachView(context, this)

        snackbar = createSnackBar()

        initRecyclerView()

        repositoryListAdapter = RepositoryListAdapter(repositoryList) { itemClick(it) }
        repositoryListRecyclerview.adapter = repositoryListAdapter

        if (repositoryList.isEmpty()) {
            presenter.getRepositories(loaderManager)
        }
    }

    private fun createSnackBar(): Snackbar {
        return Snackbar.make(view as View,
                R.string.repository_list_get_error,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry,
                        { view -> presenter.getRepositories(loaderManager) })
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)

        repositoryListRecyclerview.setHasFixedSize(true)
        repositoryListRecyclerview.layoutManager = layoutManager

        repositoryListRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager
                        .findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 3 /*we check with -3 because we need take in consideration the loading space*/) {
                    presenter.getRepositories(loaderManager)
                }
            }
        })
    }

    private fun itemClick(repository: Repository) {
        if (repository.owner != null) {
            val repositoryDetail = RepositoryDetailFragment
                    .newInstance(repository.owner.login,
                            repository.name)

            (activity as ContainerView).showDetail(repositoryDetail, repository.name)
        }
    }
}
