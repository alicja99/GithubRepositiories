package com.coddeaddict.githubrepositories.repository.api.call

import ServiceBuilder
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import retrofit2.Call
import com.coddeaddict.githubrepositories.model.Result

@KoinApiExtension
class GithubRepository : KoinComponent {
    fun getAllRepositories(query: String, pageNumber: Int ): Call<Result> {
        val request =
            ServiceBuilder.buildService(
                GithubService::class.java
            )
        lateinit var call: Call<Result>

        call = request.getAllRepositories(query, pageNumber)
        return call

    }
}