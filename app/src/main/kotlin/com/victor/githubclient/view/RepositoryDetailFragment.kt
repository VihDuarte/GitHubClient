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
import android.widget.TextView
import com.victor.githubclient.R
import com.victor.githubclient.model.PullRequest
import com.victor.githubclient.presenter.RepositoryDetailPresenter
import java.util.*

class RepositoryDetailFragment : Fragment(), RepositoryDetailView {

    internal var creator: String = ""
    internal var repository: String = ""

    private var repositoryPullsRequestRecicler: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var txtFeedBack: TextView? = null
    internal var snackbar: Snackbar? = null

    private var pullRequestList: MutableList<PullRequest>? = null
    private var repositoryPullsRequestAdapter: RepositoryPullsRequestAdapter? = null

    private var presenter: RepositoryDetailPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        presenter = RepositoryDetailPresenter()
        presenter?.attachView(context, this)

        if (arguments != null) {
            creator = arguments.getString(ARG_CREATOR)
            repository = arguments.getString(ARG_REPOSITORY)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    override fun onPause() {
        super.onPause()
        hideError()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_repository_detail, container, false)

        repositoryPullsRequestRecicler = view?.findViewById(R.id.repository_pulls_request_recyclerview) as RecyclerView
        progressBar = view?.findViewById(R.id.progress) as ProgressBar
        txtFeedBack = view?.findViewById(R.id.txt_feedback) as TextView

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (creator == null && repository == null) {
            return
        }

        val layoutManager = LinearLayoutManager(activity)

        repositoryPullsRequestRecicler?.setHasFixedSize(true)
        repositoryPullsRequestRecicler?.layoutManager = layoutManager

        if (pullRequestList == null || pullRequestList?.size == 0) {
            if (!creator.isEmpty())
                presenter?.getPullRequest(loaderManager, creator, repository)
        } else {
            repositoryPullsRequestAdapter = RepositoryPullsRequestAdapter(activity, pullRequestList!!)
            repositoryPullsRequestRecicler?.adapter = repositoryPullsRequestAdapter
            txtFeedBack?.visibility = View.GONE
        }
    }

    override fun showItems(items: MutableList<PullRequest>) {
        pullRequestList = items as ArrayList<PullRequest>
        repositoryPullsRequestAdapter = RepositoryPullsRequestAdapter(context, items)
        repositoryPullsRequestRecicler?.adapter = repositoryPullsRequestAdapter

        if (items.size == 0) {
            txtFeedBack?.setText(R.string.repository_detail_no_pull_request)
            txtFeedBack?.visibility = View.VISIBLE
        } else {
            txtFeedBack?.visibility = View.GONE
        }
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar?.visibility = View.GONE
    }

    override fun showError() {
        snackbar = Snackbar.make(view as View,
                R.string.repository_detail_error,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry,
                        { view -> presenter?.getPullRequest(loaderManager, creator, repository) })

        snackbar!!.show()
    }

    override fun hideError() {
        if (snackbar != null && snackbar!!.isShown) {
            snackbar!!.dismiss()
        }
    }

    override fun cleanData() {
        pullRequestList?.clear()
    }

    companion object {
        private val ARG_CREATOR = "creator"
        private val ARG_REPOSITORY = "repository"

        fun newInstance(creator: String, repository: String): RepositoryDetailFragment {
            val fragment = RepositoryDetailFragment()
            val args = Bundle()
            args.putString(ARG_CREATOR, creator)
            args.putString(ARG_REPOSITORY, repository)
            fragment.arguments = args
            return fragment
        }
    }
}
