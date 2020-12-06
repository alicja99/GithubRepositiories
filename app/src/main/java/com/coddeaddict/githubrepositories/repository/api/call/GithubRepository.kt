package com.coddeaddict.githubrepositories.repository.api.call

import ServiceBuilder
import com.coddeaddict.githubrepositories.model.commits.CommitsResult
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import retrofit2.Call
import com.coddeaddict.githubrepositories.model.repositoryItems.Result

@KoinApiExtension
class GithubRepository : KoinComponent {
    private val request =
        ServiceBuilder.buildService(
            GithubService::class.java
        )

    fun getAllRepositories(query: String, pageNumber: Int ): Call<Result> {
        return request.getAllRepositories(query, pageNumber)
    }

    fun getCommits(owner: String, repositoryName: String ): Call<CommitsResult> {
        return request.getCommits(owner, repositoryName)
    }
}