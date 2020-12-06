package com.coddeaddict.githubrepositories.viewmodel.repolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coddeaddict.githubrepositories.model.RepositoryItem
import com.coddeaddict.githubrepositories.repository.api.call.GithubRepository
import com.coddeaddict.githubrepositories.state.UIState
import retrofit2.Call
import retrofit2.Callback
import com.coddeaddict.githubrepositories.model.Result
import org.koin.core.component.KoinApiExtension


@KoinApiExtension
class RepoListViewModel (private val githubRepository: GithubRepository): ViewModel() {

    var isDataLoading: Boolean = false
    var pageNumber: Int = 0
    var totalResults: Int = 9999
    var repositoriesLiveData = MutableLiveData<List<RepositoryItem>>(listOf())
    var UIstateLiveData = MutableLiveData<UIState>(UIState.INITIALIZED)


    fun getRepositories( query: String, currentPageNumber: Int) {
        githubRepository.getAllRepositories( query, currentPageNumber)
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
                                pageNumber+=1
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                   repositoriesLiveData.postValue(null)
                    UIstateLiveData.postValue(UIState.ON_ERROR)
                }
            })

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
            repositoriesLiveData.postValue(listOf())
            getRepositories( query, 0)
    }
}