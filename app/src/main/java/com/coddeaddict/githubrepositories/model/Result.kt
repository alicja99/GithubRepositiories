package com.coddeaddict.githubrepositories.model

data class Result(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)