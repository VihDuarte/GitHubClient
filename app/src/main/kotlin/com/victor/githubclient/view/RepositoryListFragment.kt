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
    private var repositoryList: MutableList<Repository> = arrayListOf()
    private var repositoryListAdapter: RepositoryListAdapter? = null
    private var snackbar: Snackbar? = null

    lateinit private var presenter: RepositoryListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        presenter = RepositoryListPresenter()
        presenter.attachView(context, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_repository_list, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        if (repositoryList.isEmpty()) {
            presenter.getRepositories(loaderManager)
        } else {
            repositoryListAdapter = RepositoryListAdapter(repositoryList) { itemClick(it) }
            repositoryListRecyclerview?.adapter = repositoryListAdapter
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)

        repositoryListRecyclerview?.setHasFixedSize(true)
        repositoryListRecyclerview?.layoutManager = layoutManager

        repositoryListRecyclerview?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager
                        .findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 3 /*we check with -3 because we need take in consideration the loading space*/) {
                    presenter.getRepositories(loaderManager)
                }
            }
        })
    }

    override fun showItems(items: MutableList<Repository>) {
        if (repositoryListAdapter == null) {
            repositoryList = items
            repositoryListAdapter = RepositoryListAdapter(items) { itemClick(it) }
            repositoryListRecyclerview?.adapter = repositoryListAdapter
        } else {
            repositoryList.addAll(items)
            repositoryListAdapter!!.notifyDataSetChanged()
        }
    }

    override fun cleanData() {
        repositoryList.clear()
    }

    override fun showError() {
        snackbar = Snackbar.make(view as View,
                R.string.repository_list_get_error,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry,
                        { view -> presenter.getRepositories(loaderManager) })

        snackbar!!.show()
    }

    override fun hideError() {
        if (snackbar != null && snackbar!!.isShown) {
            snackbar!!.dismiss()
        }
    }

    private fun itemClick(repository: Repository) {
        (activity as ContainerView).showDetail(
                RepositoryDetailFragment.newInstance(
                        repository.owner!!.login,
                        repository.name), repository.name)
    }
}
