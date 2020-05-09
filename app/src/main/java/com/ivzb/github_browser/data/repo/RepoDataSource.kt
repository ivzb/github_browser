package com.ivzb.github_browser.data.repo

import com.ivzb.github_browser.model.ui.Repo

interface RepoDataSource {

    fun getRepos(user: String): List<Repo>?

    fun getRepo(repo: String): Repo?
}
