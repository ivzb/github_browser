package com.ivzb.github_browser.model.network

import com.google.gson.annotations.SerializedName
import com.ivzb.github_browser.model.ui.Repo

data class RepoResponse(

    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("owner")
    val owner: RepoOwnerResponse,

    @SerializedName("fork")
    val isFork: Boolean,

    @SerializedName("stargazers_count")
    val starsCount: Int,

    @SerializedName("watchers_count")
    val watchersCount: Int,

    @SerializedName("forks_count")
    val forksCount: Int,

    @SerializedName("language")
    val language: String?

) {

    fun asRepo() = Repo(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        description = this.description ?: "",
        owner = this.owner.login,
        isFork = this.isFork,
        starsCount = this.starsCount,
        watchersCount = this.watchersCount,
        forksCount = this.forksCount,
        language = this.language ?: ""
    )
}

data class RepoOwnerResponse(

    @SerializedName("login")
    val login: String
)
