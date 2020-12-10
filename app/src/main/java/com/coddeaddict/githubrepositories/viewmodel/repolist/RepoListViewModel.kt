package com.coddeaddict.githubrepositories.viewmodel.repolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coddeaddict.githubrepositories.model.repositoryItems.RepositoryItem
import com.coddeaddict.githubrepositories.model.repositoryItems.Result
import com.coddeaddict.githubrepositories.repository.api.call.GithubRepository
import com.coddeaddict.githubrepositories.state.UIState
import com.coddeaddict.githubrepositories.util.NoInternetException
import org.koin.core.component.KoinApiExtension
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@KoinApiExtension
class RepoListViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    private val startPageNumber = 1

    var isDataLoading: Boolean = false
    var pageNumber: Int = startPageNumber
    var totalResults: Int = 9999
    var repositoriesLiveData = MutableLiveData<List<RepositoryItem>>(listOf())
    var UIstateLiveData = MutableLiveData(UIState.INITIALIZED)
    var apiError = MutableLiveData<String>()

    fun getRepositories(query: String, currentPageNumber: Int) {
        try {
            githubRepository.getAllRepositories(query, currentPageNumber)
                .enqueue(object : Callback<Result> {
                    override fun onResponse(
                        call: Call<Result>,
                        response: Response<Result>
                    ) {
                        if (response.isSuccessful) {
                            onResponseSuccess(response)
                        }
                    }

                    override fun onFailure(call: Call<Result>, t: Throwable) {
                        onResponseFailure(call, t)
                    }
                })
        } catch (exception: NoInternetException) {
            apiError.postValue(exception.message)
        }

    }

    private fun onResponseSuccess(response: Response<Result>) {
        totalResults = response.body()!!.totalCount
        response.body()?.repositoryItems?.let {
            if (it.isEmpty()) {
                UIstateLiveData.postValue(UIState.ON_EMPTY_RESULTS)
            } else {
                val repositoriesList = response.body()?.repositoryItems
                updateRepositoriesList(repositoriesList)
                UIstateLiveData.postValue(UIState.ON_RESULT)
                incrementPageNumberByOne()
            }
        }
    }

    private fun onResponseFailure(call: Call<Result>, t: Throwable) {
        repositoriesLiveData.postValue(null)
        apiError.postValue(t.message)
    }

    private fun incrementPageNumberByOne() {
        pageNumber += 1
    }

    fun updateRepositoriesList(newRepositoryList: List<RepositoryItem>?) {
        newRepositoryList?.let { movies ->
            repositoriesLiveData.value?.let { currentMovieList ->
                val updatedMovieList = ArrayList(currentMovieList)
                updatedMovieList.addAll(movies)
                repositoriesLiveData.postValue(updatedMovieList)
            }
        }
    }

    fun checkIfThereIsScrollingPossible(totalItemCount: Int): Boolean {
        return (totalResults - 1 > totalItemCount)
    }

    fun onSearchQueryChanged(query: String) {
        if (query.isEmpty()) {
            UIstateLiveData.postValue(UIState.INITIALIZED)
        } else {
            repositoriesLiveData.postValue(listOf())
            getRepositories(query, 0)
        }

    }
}