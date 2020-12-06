package com.coddeaddict.githubrepositories.repository.api.call

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.coddeaddict.githubrepositories.model.Result

interface GithubService {
    @GET("search/repositories?sort=stars&order=desc")
    fun getAllRepositories(
        @Query("q") query: String,
        @Query("page") pageNumber: Int,
    ): Call<Result>

}