package com.ivzb.github_browser.model.user

data class User(
    val id: Long,
    val login: String,
    val name: String,
    val avatarUrl: String,
    val repos: Int,
    val followers: Int,
    val following: Int,
    val contributions: Int
) {

    fun asUserFtsEntity() = UserFtsEntity(
        id = this.id,
        login = this.login,
        name = this.name,
        avatarUrl = this.avatarUrl,
        repos = this.repos,
        followers = this.followers,
        following = this.following,
        contributions = this.contributions
    )
}
