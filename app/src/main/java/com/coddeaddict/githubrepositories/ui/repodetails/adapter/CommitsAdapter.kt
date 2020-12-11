package com.coddeaddict.githubrepositories.ui.repodetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.coddeaddict.githubrepositories.R
import com.coddeaddict.githubrepositories.model.commits.CommitsItem
import com.coddeaddict.githubrepositories.ui.repodetails.RepoDetailsFragment
import com.coddeaddict.githubrepositories.viewmodel.repodetails.RepoDetailsViewModel
import kotlinx.android.synthetic.main.commits_item.view.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class CommitsAdapter(viewModel: RepoDetailsViewModel, fragment: RepoDetailsFragment) :
    RecyclerView.Adapter<CommitsAdapter.CommitViewAdapter>() {
    private var commits = ArrayList<CommitsItem?>()
    private val maxItemAmount = 3

    init {
        viewModel.commitsLiveData.observe(fragment.viewLifecycleOwner) {
            commits.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewAdapter {
        val inflater = LayoutInflater.from(parent.context)
        return CommitViewAdapter(inflater.inflate(R.layout.commits_item, parent, false))
    }

    override fun getItemCount(): Int {
        return if (commits.size > maxItemAmount) {
            maxItemAmount
        } else {
            commits.size
        }
    }

    override fun onBindViewHolder(holder: CommitViewAdapter, position: Int) {

        commits[position]?.let {
            holder.bindData(it)
            val indexNumber = position + 1
            holder.itemView.commit_index.text = indexNumber.toString()
        }
    }

    inner class CommitViewAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: CommitsItem) {
            with(itemView) {
                commit_author_name.text = item.commit.author.name
                commit_author_email.text = item.commit.author.email
                commit_message.text = item.commit.message
            }
        }
    }
}
