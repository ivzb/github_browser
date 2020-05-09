package com.ivzb.github_browser.model.network

import com.google.gson.annotations.SerializedName
import com.ivzb.github_browser.model.ui.User

data class UserResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("login")
    val login: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("repos")
    val repos: Int,

    @SerializedName("followers")
    val followers: Int,

    @SerializedName("following")
    val following: Int
) {

    fun asUser() = User(
        id = this.id,
        login = this.login,
        name = this.name ?: "",
        avatarUrl = this.avatarUrl,
        repos = this.repos,
        followers = this.followers,
        following = this.following
    )
}
