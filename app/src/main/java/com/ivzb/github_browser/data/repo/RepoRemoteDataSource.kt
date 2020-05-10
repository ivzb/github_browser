package com.ivzb.github_browser.data.repo

import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.model.repo.RepoType

interface RepoRemoteDataSource {

    fun getRepos(user: String, type: RepoType): List<Repo>?

    fun getRepo(repo: String): Repo?
}
