package com.victor.githubclient.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victor.githubclient.R
import com.victor.githubclient.extensions.formatToString
import com.victor.githubclient.extensions.loadImage
import com.victor.githubclient.model.PullRequest
import kotlinx.android.synthetic.main.pulls_request_row.view.*

class RepositoryPullsRequestAdapter(private val items: List<PullRequest>,
                                    val itemClick: (PullRequest) -> Unit)
    : RecyclerView.Adapter<RepositoryPullsRequestAdapter.RepositoryPullsRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryPullsRequestViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.pulls_request_row, parent, false)

        return RepositoryPullsRequestViewHolder(v, itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RepositoryPullsRequestViewHolder, position: Int) {
        holder.binItem(items[position])
    }

    class RepositoryPullsRequestViewHolder(itemView: View,
                                           val itemClick: (PullRequest) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        fun binItem(item: PullRequest) {
            with(item) {
                itemView.txtName?.text = item.user?.name
                itemView.txtUserName?.text = item.user?.login
                itemView.txtTitle?.text = item.title
                itemView.txtBody?.text = item.body
                itemView.txtDate?.text = item.createdAt?.formatToString()
                itemView.imgProfile?.loadImage(item.user?.avatarUrl, R.drawable.avatar)

                itemView.setOnClickListener { view ->
                    itemClick(item)
                }
            }
        }
    }
}