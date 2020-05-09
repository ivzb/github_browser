package com.ivzb.github_browser.model.network

import com.google.gson.annotations.SerializedName
import com.ivzb.github_browser.model.ui.Repo

data class RepoResponse(

    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("fork")
    val isFork: Boolean,

    @SerializedName("stargazers_count")
    val starsCount: Int,

    @SerializedName("watchers_count")
    val watchersCount: Int,

    @SerializedName("language")
    val language: String?
) {

    fun asRepo() = Repo(
        id = this.id,
        name = this.name,
        description = this.description ?: "",
        isFork = this.isFork,
        starsCount = this.starsCount,
        watchersCount = this.watchersCount,
        language = this.language ?: ""
    )
}
