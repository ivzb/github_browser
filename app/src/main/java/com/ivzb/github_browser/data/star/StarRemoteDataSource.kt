package com.ivzb.github_browser.data.star

interface StarRemoteDataSource {

    fun isStarred(repo: String): Boolean?

    fun star(repo: String): Boolean?

    fun unstar(repo: String): Boolean?
}
