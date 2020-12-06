package com.coddeaddict.githubrepositories.repository.api.call

import com.coddeaddict.githubrepositories.model.commits.CommitsResult
import com.coddeaddict.githubrepositories.model.repositoryItems.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("search/repositories?sort=stars&order=desc")
    fun getAllRepositories(
        @Query("q") query: String,
        @Query("page") pageNumber: Int,
    ): Call<Result>

    @GET("repos/{owner}/{repositoryName}/commits")
    fun getCommits(
        @Path("owner") owner: String,
        @Path("repositoryName") repositoryName: String
    ): Call<CommitsResult>
}