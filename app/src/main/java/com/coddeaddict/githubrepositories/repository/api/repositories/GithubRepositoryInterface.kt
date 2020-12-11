package com.coddeaddict.githubrepositories.repository.api.repositories

import com.coddeaddict.githubrepositories.model.commits.CommitsResult
import com.coddeaddict.githubrepositories.model.repositoryItems.Result
import retrofit2.Call

interface GithubRepositoryInterface {
    suspend fun getAllRepositories(query: String, pageNumber: Int): Call<Result>

    suspend fun getCommits(owner: String, repositoryName: String): Call<CommitsResult>
}