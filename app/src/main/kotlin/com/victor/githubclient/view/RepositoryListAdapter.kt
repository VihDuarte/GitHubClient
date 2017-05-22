package com.victor.githubclient.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victor.githubclient.R
import com.victor.githubclient.extensions.formatCount
import com.victor.githubclient.extensions.loadImage
import com.victor.githubclient.model.Repository
import kotlinx.android.synthetic.main.loader_item_layout.view.*
import kotlinx.android.synthetic.main.repository_list_row.view.*

class RepositoryListAdapter(val items: List<Repository>,
                            val itemClick: (Repository) -> Unit) : RecyclerView.Adapter<RepositoryListAdapter.RepositoryListViewHolderContract>() {
    private val VIEW_TYPE_ITEM = 1
    private val VIEW_TYPE_LOADER = 2

    override fun getItemCount(): Int {
        if (items.isEmpty()) {
            return 0
        }

        return items.size + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryListViewHolderContract {
        if (viewType == VIEW_TYPE_LOADER) {
            val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.loader_item_layout, parent, false)

            return LoaderViewHolder(v)
        } else if (viewType == VIEW_TYPE_ITEM) {
            val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.repository_list_row, parent, false)
            return RepositoryListViewHolder(v, itemClick)
        }

        throw IllegalArgumentException("Invalid ViewType: " + viewType)
    }

    override fun onBindViewHolder(holder: RepositoryListViewHolderContract, position: Int) {
        val type = getItemViewType(position)

        if (type == VIEW_TYPE_LOADER)
            holder.binItem(null)
        else if (type == VIEW_TYPE_ITEM) {
            holder.binItem(items[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position != 0 && position == itemCount - 1) {
            return VIEW_TYPE_LOADER
        } else {
            return VIEW_TYPE_ITEM
        }
    }

    abstract inner class RepositoryListViewHolderContract(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        abstract fun binItem(item: Repository?)
    }

    inner class RepositoryListViewHolder(itemView: View,
                                         val itemClick: (Repository) -> Unit)
        : RepositoryListViewHolderContract(itemView) {
        override fun binItem(item: Repository?) {
           item?.let {
                with(it) {
                    itemView.txtTitle?.text = name
                    itemView.txtDescription?.text = description
                    itemView.txtForkCount?.text = forksCount?.formatCount()
                    itemView.txtStarCount?.text = stargazersCount?.formatCount()

                    item.owner?.let {
                        itemView.txtName?.text = it.name
                        itemView.txtUserName?.text = it.login
                        itemView.imgProfile?.loadImage(it.avatarUrl, R.drawable.avatar)

                        itemView.setOnClickListener {
                            itemClick(item)
                        }
                    }
                }
            }
        }
    }

    inner class LoaderViewHolder(itemView: View)
        : RepositoryListViewHolderContract(itemView) {
        override fun binItem(item: Repository?) {
            itemView.progress.visibility = View.VISIBLE
        }
    }
}
