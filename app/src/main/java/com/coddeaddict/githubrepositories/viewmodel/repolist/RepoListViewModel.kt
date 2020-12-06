package com.coddeaddict.githubrepositories.viewmodel.repolist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coddeaddict.githubrepositories.model.repositoryItems.RepositoryItem
import com.coddeaddict.githubrepositories.model.repositoryItems.Result
import com.coddeaddict.githubrepositories.repository.api.call.GithubRepository
import com.coddeaddict.githubrepositories.state.UIState
import org.koin.core.component.KoinApiExtension
import retrofit2.Call
import retrofit2.Callback


@KoinApiExtension
class RepoListViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    private val startPageNumber = 1

    var isDataLoading: Boolean = false
    var pageNumber: Int = startPageNumber
    var totalResults: Int = 9999
    var repositoriesLiveData = MutableLiveData<List<RepositoryItem>>(listOf())
    var UIstateLiveData = MutableLiveData<UIState>(UIState.INITIALIZED)


    fun getRepositories(query: String, currentPageNumber: Int) {
        githubRepository.getAllRepositories(query, currentPageNumber)
            .enqueue(object : Callback<Result> {
                override fun onResponse(
                    call: Call<Result>,
                    response: retrofit2.Response<Result>
                ) {
                    if (response.isSuccessful) {
                        totalResults = response.body()!!.total_count
                        response.body()?.repositoryItems?.let {
                            if (it.isEmpty() && currentPageNumber == 0) {
                                UIstateLiveData.postValue(UIState.ON_EMPTY_RESULTS)
                            } else {
                                val repositoriesList = response.body()?.repositoryItems
                                updateRepositoriesList(repositoriesList)
                                UIstateLiveData.postValue(UIState.ON_RESULT)
                                incrementPageNumberByOne()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    repositoriesLiveData.postValue(null)
                    UIstateLiveData.postValue(UIState.ON_ERROR)
                }
            })
        Log.d("page", pageNumber.toString())
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