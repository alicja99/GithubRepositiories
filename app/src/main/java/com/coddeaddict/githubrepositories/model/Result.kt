package com.coddeaddict.githubrepositories.model

import com.google.gson.annotations.SerializedName

data class Result(
    val incomplete_results: Boolean,
    @SerializedName("items")
    val repositoryItems: List<RepositoryItem>,
    val total_count: Int
)