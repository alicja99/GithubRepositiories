package com.coddeaddict.githubrepositories.ui.repodetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.coddeaddict.githubrepositories.R
import com.coddeaddict.githubrepositories.constants.Constants
import com.coddeaddict.githubrepositories.databinding.FragmentRepoDetailsBinding
import com.coddeaddict.githubrepositories.model.repositoryItems.RepositoryItem
import com.coddeaddict.githubrepositories.state.UICommitsState
import com.coddeaddict.githubrepositories.ui.repodetails.adapter.CommitsAdapter
import com.coddeaddict.githubrepositories.viewmodel.repodetails.RepoDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension


@KoinApiExtension
class RepoDetailsFragment : Fragment() {
    companion object {
        const val REPOSITORY_ITEM_KEY = "REPOSITORY_ITEM_KEY"
    }

    private lateinit var binding: FragmentRepoDetailsBinding
    private lateinit var repositoryItem: RepositoryItem
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: CommitsAdapter? = null
    private val viewModel: RepoDetailsViewModel by viewModel()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        repositoryItem = arguments?.getSerializable(REPOSITORY_ITEM_KEY) as RepositoryItem
        setDataIntoFields(repositoryItem)
        initializeFragment()
    }

    private fun initializeFragment() {
        setUpRecyclerView()
        setUpAdapter()
        observeLiveData()
        setUpButtonsOnClick()
        fetchCommits()
    }

    private fun fetchCommits() {
        viewModel.getCommits(repositoryItem.owner.login, repositoryItem.name)
    }

    private fun setUpButtonsOnClick() {
        val repositoryUrl = "${Constants.GITHUB_BASE_URL}/${repositoryItem.full_name}"

        viewOnlineButtonOnClick(repositoryUrl)
        goBackButtonOnClick()
        shareRepoButtonOnClick(repositoryUrl)
    }

    private fun viewOnlineButtonOnClick(repositoryUrl: String) {
        binding.viewOnlineButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(repositoryUrl)))
        }
    }

    private fun goBackButtonOnClick() {
        binding.goBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun shareRepoButtonOnClick(repositoryUrl: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, repositoryUrl)
        }

        binding.shareRepoButton.setOnClickListener {
            startActivity(Intent.createChooser(intent, "Share Repo"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setDataIntoFields(repositoryItem: RepositoryItem) {
        binding.repositoryAuthorName.text = repositoryItem.owner.login
        val starsString = String.format(
            resources.getString(
                R.string.number_of_stars,
                repositoryItem.stargazers_count
            )
        )
        binding.repositoryStarsDetails.text = starsString
        binding.repoTitle.text = repositoryItem.name
        Glide.with(this)
            .load(repositoryItem.owner.avatarUrl)
            .into(binding.repositoryImageDetails)
    }

    private fun observeLiveData() {
        viewModel.apiError.observe(viewLifecycleOwner) {
            showOnError(it)
        }

        viewModel.UICommitsLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                UICommitsState.NO_COMMITS -> {
                    showEmptyResults()
                }
                UICommitsState.ON_RESULT -> {
                    hideProgressBar()
                }
                UICommitsState.LOADING -> {
                    showProgressBar()
                }
                UICommitsState.EMPTY_REPOSITORY->{
                    showOnError(resources.getString(R.string.empty_repository))
                }
                else -> {
                    showOnError()
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity)
        binding.commitsRecyclerview.layoutManager = linearLayoutManager
    }

    private fun setUpAdapter() {
        viewModel.let { viewModel ->
            adapter = CommitsAdapter(viewModel, this)
            binding.commitsRecyclerview.adapter = adapter
        }
        setUpRecyclerViewDivider()
    }

    private fun setUpRecyclerViewDivider() {
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.recycler_view_divider
            )
        }?.let { itemDecorator.setDrawable(it) }

        binding.commitsRecyclerview.addItemDecoration(
            itemDecorator
        )
    }

    private fun showEmptyResults() {
        binding.commitsRecyclerview.visibility = View.GONE
        binding.resultsTextView.visibility = View.VISIBLE
        binding.resultsTextView.text = resources.getString(R.string.no_results)
        binding.progressbarDetails.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.commitsRecyclerview.visibility = View.GONE
        binding.resultsTextView.visibility = View.GONE
        binding.progressbarDetails.visibility = View.VISIBLE
    }

    private fun showOnError(message: String? = null) {
        if (!message.isNullOrEmpty()) {
            binding.resultsTextView.text = message
        } else {
            binding.resultsTextView.text = resources.getString(R.string.check_internet_connection)
        }

        binding.commitsRecyclerview.visibility = View.GONE
        binding.resultsTextView.visibility = View.VISIBLE
        binding.progressbarDetails.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.commitsRecyclerview.visibility = View.VISIBLE
        binding.resultsTextView.visibility = View.GONE
        binding.progressbarDetails.visibility = View.GONE
    }

}
