package com.coddeaddict.githubrepositories.model.repositoryItems

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val id: Int,
    val login: String,
    val url: String
): Serializable {
    constructor() : this("" ,0,"" , "")
}