package com.ivzb.github_browser.model.user

import com.google.gson.annotations.SerializedName
import com.ivzb.github_browser.model.user.User

data class UserResponse(

    @SerializedName("id")
    val id: Long,

    @SerializedName("login")
    val login: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("public_repos")
    val repos: Int,

    @SerializedName("followers")
    val followers: Int,

    @SerializedName("following")
    val following: Int,

    @SerializedName("contributions")
    val contributions: Int
) {

    fun asUser() = User(
        id = this.id,
        login = this.login,
        name = this.name ?: "",
        avatarUrl = this.avatarUrl,
        repos = this.repos,
        followers = this.followers,
        following = this.following,
        contributions = this.contributions
    )
}
