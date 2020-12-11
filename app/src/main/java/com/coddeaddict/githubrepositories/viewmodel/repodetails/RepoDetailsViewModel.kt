package com.coddeaddict.githubrepositories.viewmodel.repodetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coddeaddict.githubrepositories.model.commits.CommitsItem
import com.coddeaddict.githubrepositories.model.commits.CommitsResult
import com.coddeaddict.githubrepositories.repository.api.repositories.GithubRepository
import com.coddeaddict.githubrepositories.state.UICommitsState
import com.coddeaddict.githubrepositories.util.NoInternetException
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@KoinApiExtension
class RepoDetailsViewModel(private val githubRepository: GithubRepository) : ViewModel() {

    var commitsLiveData = MutableLiveData<List<CommitsItem>>(listOf())
    var UICommitsLiveData = MutableLiveData<UICommitsState>()
    var apiError = MutableLiveData<String>()


    fun getCommits(owner: String, repositoryName: String) {
        UICommitsLiveData.postValue(UICommitsState.LOADING)

        viewModelScope.launch {
            try {
                githubRepository.getCommits(owner, repositoryName)
                    .enqueue(object : Callback<CommitsResult> {
                        override fun onResponse(
                            call: Call<CommitsResult>,
                            response: Response<CommitsResult>
                        ) {
                            if (response.isSuccessful) {
                                onResponseSuccess(response)

                            } else {
                                onResponseErrorHTTPCode(response.code())
                            }
                        }

                        override fun onFailure(call: Call<CommitsResult>, t: Throwable) {
                            onResponseFailure(t)
                        }
                    })

            } catch (exception: NoInternetException) {
                apiError.postValue(exception.message)
            }
        }
    }

    private fun onResponseSuccess(response: Response<CommitsResult>) {
        val commitList = response.body()

        val totalResults = response.body()?.size
        if (totalResults == 0) {
            UICommitsLiveData.postValue(UICommitsState.NO_COMMITS)
        } else {
            updateCommitList(commitList)
            UICommitsLiveData.postValue(UICommitsState.ON_RESULT)
        }
    }

    private fun onResponseFailure(t: Throwable) {
        commitsLiveData.postValue(null)
        apiError.postValue(t.message)
    }

    private fun onResponseErrorHTTPCode(responseCode: Int) {
        if (responseCode == 409) {
            UICommitsLiveData.postValue(UICommitsState.EMPTY_REPOSITORY)
        }
    }

    private fun updateCommitList(commitList: CommitsResult?) {
        commitsLiveData.postValue(commitList)
    }
}