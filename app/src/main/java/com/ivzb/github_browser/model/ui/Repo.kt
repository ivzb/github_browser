package com.ivzb.github_browser.model.ui

import com.ivzb.github_browser.model.db.RepoFtsEntity

data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String,
    val owner: String,
    val isFork: Boolean,
    val starsCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    val language: String
) {

    fun asRepoFtsEntity() = RepoFtsEntity(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        description = this.description,
        owner = this.owner,
        isFork = this.isFork,
        starsCount = this.starsCount,
        watchersCount = this.watchersCount,
        forksCount = this.forksCount,
        language = this.language
    )
}
