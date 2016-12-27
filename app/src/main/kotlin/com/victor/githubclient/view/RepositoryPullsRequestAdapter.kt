package com.victor.githubclient.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.victor.githubclient.R
import com.victor.githubclient.model.PullRequest
import com.victor.githubclient.utils.ImageLoader


class RepositoryPullsRequestAdapter(private val context: Context, private val items: List<PullRequest>) : RecyclerView.Adapter<RepositoryPullsRequestAdapter.RepositoryPullsRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryPullsRequestViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.pulls_request_row, parent, false)

        val vh = RepositoryPullsRequestViewHolder(v)
        return vh
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RepositoryPullsRequestViewHolder, position: Int) {
        val item = items[position]

        holder.txtName?.text = item.user?.name
        holder.txtUserName?.text = item.user?.login
        holder.txtTitle?.text = item.title
        holder.txtBody?.text = item.body
        holder.txtDate?.text = item.createdAtFormated

        if (holder.imgProfile != null)
            ImageLoader.loadImage(item.user?.avatarUrl, holder.imgProfile!!, R.drawable.avatar)

        holder.layoutParent?.setOnClickListener { view ->
            context.startActivity(
                    Intent(Intent.ACTION_VIEW,
                            Uri.parse(item.htmlUrl)))
        }
    }

    class RepositoryPullsRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtUserName: TextView? = null
        var txtName: TextView? = null
        var imgProfile: ImageView? = null
        var txtTitle: TextView? = null
        var txtBody: TextView? = null
        var txtDate: TextView? = null
        var layoutParent: RelativeLayout? = null

        init {
            layoutParent = itemView.findViewById(R.id.layout_parent) as RelativeLayout
            txtDate = itemView.findViewById(R.id.txt_date) as TextView
            txtBody = itemView.findViewById(R.id.txt_body) as TextView
            txtTitle = itemView.findViewById(R.id.txt_title) as TextView
            imgProfile = itemView.findViewById(R.id.img_profile) as ImageView
            txtName = itemView.findViewById(R.id.txt_name) as TextView
            txtUserName = itemView.findViewById(R.id.txt_user_name) as TextView
        }
    }
}