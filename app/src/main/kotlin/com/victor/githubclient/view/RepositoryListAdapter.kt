package com.victor.githubclient.view

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.victor.githubclient.R
import com.victor.githubclient.loader.GitHubLoaderManager
import com.victor.githubclient.model.Repository
import com.victor.githubclient.presenter.RepositoryListPresenter
import com.victor.githubclient.utils.DownloadImageTask
import com.victor.githubclient.utils.IntUtils

class RepositoryListAdapter(private val activity: Activity, val items: List<Repository>) : RecyclerView.Adapter<RepositoryListAdapter.RepositoryListViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(position: Int): Repository {
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryListViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.repository_list_row, parent, false)

        val vh = RepositoryListViewHolder(v)

        return vh
    }

    override fun onBindViewHolder(holder: RepositoryListViewHolder, position: Int) {
        val item = items[position]

        holder.txtName!!.text = item.owner?.name
        holder.txtUserName!!.text = item.owner?.login
        holder.txtTitle!!.text = item.name
        holder.txtDescription!!.text = item.description
        holder.txtForkCount!!.text = IntUtils.formatCount(item.forksCount!!)
        holder.txtStarCount!!.text = IntUtils.formatCount(item.stargazersCount!!)
        holder.imgProfile!!.setImageResource(R.drawable.avatar)

        DownloadImageTask(holder.imgProfile!!).execute(item.owner?.avatarUrl)

        holder.layoutParent!!.setOnClickListener { view ->
            //show detail
        }
    }


    inner class RepositoryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtUserName: TextView? = null
        var txtName: TextView? = null
        var imgProfile: ImageView? = null
        var txtTitle: TextView? = null
        var txtDescription: TextView? = null
        var txtForkCount: TextView? = null
        var txtStarCount: TextView? = null
        var layoutParent: RelativeLayout? = null

        init {
            txtUserName = itemView.findViewById(R.id.txt_user_name) as TextView?;
            txtName = itemView.findViewById(R.id.txt_name) as TextView?;
            imgProfile = itemView.findViewById(R.id.img_profile) as ImageView?;
            txtTitle = itemView.findViewById(R.id.txt_title) as TextView?;
            txtDescription = itemView.findViewById(R.id.txt_description) as TextView?;
            txtForkCount = itemView.findViewById(R.id.txt_fork_count) as TextView?;
            txtStarCount = itemView.findViewById(R.id.txt_star_count) as TextView?;
            layoutParent = itemView.findViewById(R.id.layout_parent) as RelativeLayout?;

        }
    }
}
