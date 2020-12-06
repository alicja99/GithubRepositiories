package com.coddeaddict.githubrepositories.model.repositoryItems

import com.coddeaddict.githubrepositories.model.repositoryItems.RepositoryItem
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Result(
    val incomplete_results: Boolean,
    @SerializedName("items")
    val repositoryItems: List<RepositoryItem>,
    val total_count: Int
): Serializable {
    constructor() : this(false, listOf(), 0)
}