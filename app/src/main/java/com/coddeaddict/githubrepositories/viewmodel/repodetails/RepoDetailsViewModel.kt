package com.coddeaddict.githubrepositories.viewmodel.repodetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coddeaddict.githubrepositories.model.commits.CommitsItem
import com.coddeaddict.githubrepositories.model.commits.CommitsResult
import com.coddeaddict.githubrepositories.repository.api.call.GithubRepository
import com.coddeaddict.githubrepositories.state.UICommitsState
import org.koin.core.component.KoinApiExtension
import retrofit2.Call
import retrofit2.Callback

@KoinApiExtension
class RepoDetailsViewModel(private val githubRepository: GithubRepository) : ViewModel() {

    var commitsLiveData = MutableLiveData<List<CommitsItem>>(listOf())
    var UICommitsLiveData = MutableLiveData<UICommitsState>()

    fun getCommits(owner: String, repositoryName: String) {
        githubRepository.getCommits(owner, repositoryName)
            .enqueue(object : Callback<CommitsResult> {
                override fun onResponse(
                    call: Call<CommitsResult>,
                    response: retrofit2.Response<CommitsResult>
                ) {
                    if (response.isSuccessful) {
                        val commitList = response.body()

                        val totalResults = response.body()?.size
                        if (totalResults == 0) {
                            UICommitsLiveData.postValue(UICommitsState.NO_COMMITS)
                        } else {
                            updateCommitList(commitList)
                            UICommitsLiveData.postValue(UICommitsState.ON_RESULT)
                        }

                    }
                }

                override fun onFailure(call: Call<CommitsResult>, t: Throwable) {
                    commitsLiveData.postValue(null)
                    UICommitsLiveData.postValue(UICommitsState.ON_ERROR)
                }
            })

    }

    private fun updateCommitList(commitList: CommitsResult?) {
        commitsLiveData.postValue(commitList)
    }
}