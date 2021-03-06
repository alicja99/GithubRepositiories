package com.coddeaddict.githubrepositories.ui.repolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.coddeaddict.githubrepositories.R
import com.coddeaddict.githubrepositories.model.repositoryItems.RepositoryItem
import com.coddeaddict.githubrepositories.ui.repolist.adapter.viewholder.base.BaseViewHolder
import com.coddeaddict.githubrepositories.ui.repolist.adapter.viewholder.footer.FooterViewHolder
import com.coddeaddict.githubrepositories.viewmodel.repolist.RepoListViewModel
import kotlinx.android.synthetic.main.repo_item.view.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class RepoListAdapter(viewModel: RepoListViewModel, fragment: Fragment) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private var repositories = ArrayList<RepositoryItem?>()
    private var isProgressBarVisible = false
    var onItemClick: ((RepositoryItem) -> Unit)? = null


    init {
        viewModel.repositoriesLiveData.observe(fragment.viewLifecycleOwner) {
            viewModel.isDataLoading = false
            hideFooterProgressBar()
            repositories.clear()
            it?.let {
                repositories.addAll(it)
                notifyDataSetChanged()
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_FOOTER -> {
                FooterViewHolder(
                    inflater.inflate(R.layout.footer_progress_bar, parent, false)
                )
            }

            TYPE_LAYOUT -> {
                RowViewHolder(inflater.inflate(R.layout.repo_item, parent, false))
            }

            else -> RowViewHolder(inflater.inflate(R.layout.repo_item, parent, false))
        }

    }

    override fun getItemCount(): Int = repositories.size


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is RowViewHolder) {
            repositories[position]?.let { holder.bindData(it) }
        }

    }

    inner class RowViewHolder(itemView: View) : BaseViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                repositories[adapterPosition]?.let { it1 -> onItemClick?.invoke(it1) }
            }
        }

        fun bindData(item: RepositoryItem) {
            with(itemView) {
                Glide.with(context)
                    .load(item.owner.avatarUrl)
                    .into(author_image)
                commit_author_name.text = item.name
                commit_author_email.text = item.stargazers_count.toString()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionFooter(position)) {
            TYPE_FOOTER
        } else TYPE_LAYOUT
    }

    private fun isPositionFooter(position: Int): Boolean {
        return repositories[position] == null
    }

    fun showFooterProgressBar() {
        isProgressBarVisible = true
        repositories.add(null)
        notifyItemInserted(repositories.size - 1)
    }

    private fun hideFooterProgressBar() {
        isProgressBarVisible = false
        val position = repositories.size - 1
        if (position >= 0) {
            repositories.removeAt(position)
            notifyDataSetChanged()
        }
    }


    companion object {
        const val TYPE_FOOTER: Int = 0
        const val TYPE_LAYOUT: Int = 1
    }
}

