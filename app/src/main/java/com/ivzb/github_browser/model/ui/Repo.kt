package com.ivzb.github_browser.model.ui

data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String,
    val isFork: Boolean,
    val starsCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    val language: String
)
