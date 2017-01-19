package com.victor.githubclient.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victor.githubclient.R
import com.victor.githubclient.extensions.formatCount
import com.victor.githubclient.extensions.loadImage
import com.victor.githubclient.model.Repository
import kotlinx.android.synthetic.main.repository_list_row.view.*

class RepositoryListAdapter(val items: List<Repository>,
                            val itemClick: (Repository) -> Unit) : RecyclerView.Adapter<RepositoryListAdapter.RepositoryListViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryListViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.repository_list_row, parent, false)
        return RepositoryListViewHolder(v, itemClick)
    }

    override fun onBindViewHolder(holder: RepositoryListViewHolder, position: Int) {
        holder.binItem(items[position])
    }

    inner class RepositoryListViewHolder(itemView: View,
                                         val itemClick: (Repository) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        fun binItem(item: Repository) {
            with(item) {
                itemView.txtName?.text = item.owner?.name
                itemView.txtUserName?.text = item.owner?.login
                itemView.txtTitle?.text = item.name
                itemView.txtDescription?.text = item.description
                itemView.txtForkCount?.text = item.forksCount?.formatCount()
                itemView.txtStarCount?.text = item.stargazersCount?.formatCount()
                itemView.imgProfile?.loadImage(item.owner?.avatarUrl, R.drawable.avatar)

                if (item.owner != null) {
                    itemView.setOnClickListener {
                        itemClick(item)
                    }
                }
            }
        }
    }
}
