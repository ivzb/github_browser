package com.ivzb.github_browser.model.ui

data class User(
    val id: Long,
    val login: String,
    val name: String,
    val avatarUrl: String,
    val repos: Int,
    val followers: Int,
    val following: Int
)
