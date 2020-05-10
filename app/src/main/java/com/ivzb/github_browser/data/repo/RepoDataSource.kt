package com.ivzb.github_browser.data.repo

import com.ivzb.github_browser.model.repo.Repo

interface RepoDataSource {

    fun getOwnRepos(user: String): List<Repo>?

    fun getStarredRepos(user: String): List<Repo>?

    fun getRepo(repo: String): Repo?

    fun getSearchRepos(query: String): List<Repo>?
}
