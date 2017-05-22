package com.victor.githubclient.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victor.githubclient.R
import com.victor.githubclient.model.PullRequest
import com.victor.githubclient.presenter.RepositoryDetailPresenter
import kotlinx.android.synthetic.main.fragment_repository_detail.*
import java.util.*

class RepositoryDetailFragment : Fragment(), RepositoryDetailView {

    private var creator: String = ""
    private var repository: String = ""

    private var snackbar: Snackbar? = null

    private var pullRequestList: MutableList<PullRequest> = arrayListOf()
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
        return inflater?.inflate(R.layout.fragment_repository_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)

        repositoryPullsRequestRecyclerview?.setHasFixedSize(true)
        repositoryPullsRequestRecyclerview?.layoutManager = layoutManager

        if (pullRequestList.isEmpty()) {
            if (!creator.isEmpty())
                presenter?.getPullRequest(loaderManager, creator, repository)
        } else {
            repositoryPullsRequestAdapter = RepositoryPullsRequestAdapter(pullRequestList) { itemClick(it) }
            repositoryPullsRequestRecyclerview?.adapter = repositoryPullsRequestAdapter
            txtFeedback?.visibility = View.GONE
        }
    }

    override fun showItems(items: MutableList<PullRequest>) {
        pullRequestList = items as ArrayList<PullRequest>
        repositoryPullsRequestAdapter = RepositoryPullsRequestAdapter(items) { itemClick(it) }
        repositoryPullsRequestRecyclerview?.adapter = repositoryPullsRequestAdapter

        if (items.size == 0) {
            txtFeedback?.setText(R.string.repository_detail_no_pull_request)
            txtFeedback?.visibility = View.VISIBLE
        } else {
            txtFeedback?.visibility = View.GONE
        }
    }

    override fun showProgress() {
        progress?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress?.visibility = View.GONE
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
        pullRequestList.clear()
    }

    private fun itemClick(item: PullRequest) {
        context.startActivity(
                Intent(Intent.ACTION_VIEW,
                        Uri.parse(item.htmlUrl)))
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
