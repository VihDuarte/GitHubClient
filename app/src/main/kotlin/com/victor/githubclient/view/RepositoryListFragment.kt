package com.victor.githubclient.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.victor.githubclient.R
import com.victor.githubclient.model.Repository
import com.victor.githubclient.presenter.RepositoryListPresenter

/**
 * A simple [Fragment] subclass.
 */
class RepositoryListFragment : Fragment(), RepositoryListView {
    internal var repositoryListRecicler: RecyclerView? = null

    internal var progressBar: ProgressBar? = null

    protected var repositoryList: MutableList<Repository>? = null
    private var repositoryListAdapter: RepositoryListAdapter? = null
    internal var snackbar: Snackbar? = null

    var presenter: RepositoryListPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        presenter = RepositoryListPresenter()
        presenter?.attachView(context, this)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar!!.setTitle(R.string.respository_list_title)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.dettachView()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_repository_list, container, false)

        repositoryListRecicler = view.findViewById(R.id.repository_list_recyclerview) as RecyclerView
        progressBar = view.findViewById(R.id.progress) as ProgressBar

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        if (repositoryList == null || repositoryList!!.size == 0) {
            presenter!!.getRepositories(loaderManager)
        } else {
            repositoryListAdapter = RepositoryListAdapter(repositoryList!!)
            repositoryListRecicler!!.adapter = repositoryListAdapter
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)

        repositoryListRecicler!!.setHasFixedSize(true)
        repositoryListRecicler!!.layoutManager = layoutManager

        repositoryListRecicler!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager
                        .findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                    presenter!!.getRepositories(loaderManager)
                }
            }
        })
    }

    override fun showItems(items: MutableList<Repository>) {
        if (repositoryListAdapter == null) {
            repositoryList = items
            repositoryListAdapter = RepositoryListAdapter(items)
            repositoryListRecicler!!.adapter = repositoryListAdapter
        } else {
            repositoryList!!.addAll(items)
            repositoryListAdapter!!.notifyDataSetChanged()
        }
    }

    override fun cleanData() {
        if (repositoryList != null) {
            repositoryList!!.clear()
        }
    }

    override fun showError() {

        snackbar = Snackbar.make(view as View,
                R.string.repository_list_get_error,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry,
                        { view -> presenter!!.getRepositories(loaderManager) })

        snackbar!!.show()
    }

    override fun hideError() {
        if (snackbar != null && snackbar!!.isShown) {
            snackbar!!.dismiss()
        }
    }

    override fun showProgress() {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar!!.visibility = View.GONE
    }
}
