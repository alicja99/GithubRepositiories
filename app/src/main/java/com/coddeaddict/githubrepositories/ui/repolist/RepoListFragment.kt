package com.coddeaddict.githubrepositories.ui.repolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coddeaddict.githubrepositories.R
import com.coddeaddict.githubrepositories.databinding.FragmentRepoListBinding
import com.coddeaddict.githubrepositories.state.UIState
import com.coddeaddict.githubrepositories.ui.repodetails.RepoDetailsFragment.Companion.REPOSITORY_ITEM_KEY
import com.coddeaddict.githubrepositories.ui.repolist.adapter.RepoListAdapter
import com.coddeaddict.githubrepositories.viewmodel.repolist.RepoListViewModel
import com.jakewharton.rxbinding2.widget.queryTextChanges
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import java.util.concurrent.TimeUnit

@KoinApiExtension
class RepoListFragment : Fragment() {

    private lateinit var binding: FragmentRepoListBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel: RepoListViewModel by viewModel()
    private var adapter: RepoListAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initializeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initializeFragment() {
        setUpSearchView()
        setUpRecyclerView()
        setUpAdapter()
        observeLiveData()
    }

    private fun setUpRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity)
        binding.repositoriesRecyclerview.layoutManager = linearLayoutManager

        binding.repositoriesRecyclerview.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount: Int = linearLayoutManager.itemCount
                val lastVisibleItem: Int = linearLayoutManager.findLastVisibleItemPosition()

                viewModel.let {
                    if (!it.isDataLoading && lastVisibleItem == totalItemCount - 1) {
                        it.isDataLoading = true

                        if (it.checkIfThereIsScrollingPossible(totalItemCount)) {
                            adapter?.showFooterProgressBar()
                            runBlocking {
                                it.getRepositories(
                                    binding.searchview.query.toString(),
                                    viewModel.pageNumber,
                                )
                            }

                        }
                    }
                }
            }
        })
    }


    @SuppressLint("CheckResult")
    private fun setUpSearchView() {
        binding.searchview.clearFocus()

        binding.searchview.queryTextChanges().skip(2)
            .map { it.toString() }
            .doOnNext {
                viewModel.UIstateLiveData.postValue(UIState.LOADING)
            }
            .debounce(800, TimeUnit.MILLISECONDS)
            .subscribe {
                if (binding.searchview.hasFocus()) {
                    runBlocking {
                        viewModel.onSearchQueryChanged(it)
                    }

                }
            }
    }

    private fun observeLiveData() {
        viewModel.apiError.observe(viewLifecycleOwner) {
            showOnError(it)
        }

        viewModel.UIstateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                UIState.LOADING -> {
                    showProgressBar()
                }
                UIState.ON_RESULT -> {
                    hideProgressBar()
                }
                UIState.ON_EMPTY_RESULTS -> {
                    showEmptyResults()
                }
                UIState.INITIALIZED -> {
                    showSearchRepositoriesTextLabel()
                }
                else -> {
                    showOnError()
                }
            }
        }
    }

    private fun setUpAdapter() {
        viewModel.let { viewModel ->
            adapter = RepoListAdapter(viewModel, this)
            binding.repositoriesRecyclerview.adapter = adapter
        }

        adapter!!.onItemClick = {
            val bundle = Bundle()
            bundle.putSerializable(REPOSITORY_ITEM_KEY, it)
            findNavController().navigate(
                R.id.action_repoListFragment_to_repoDetailsFragment,
                bundle
            )
        }
    }

    private fun showEmptyResults() {
        binding.repositoriesRecyclerview.visibility = View.GONE
        binding.resultsTextView.visibility = View.VISIBLE
        binding.resultsTextView.text = resources.getString(R.string.no_results)
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.repositoriesRecyclerview.visibility = View.GONE
        binding.resultsTextView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showOnError(message: String? = null) {
        if (!message.isNullOrEmpty()) {
            binding.resultsTextView.text = message
        } else {
            binding.resultsTextView.text = resources.getString(R.string.check_internet_connection)
        }

        binding.repositoriesRecyclerview.visibility = View.GONE
        binding.resultsTextView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.repositoriesRecyclerview.visibility = View.VISIBLE
        binding.resultsTextView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun showSearchRepositoriesTextLabel() {
        binding.repositoriesRecyclerview.visibility = View.GONE
        binding.resultsTextView.text = resources.getString(R.string.query_repositories)
        binding.resultsTextView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }
}