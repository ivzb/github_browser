package com.ivzb.github_browser.data.repo

import androidx.lifecycle.LiveData
import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.model.repo.RepoType

interface RepoLocalDataSource {

    fun fetchRepo(repo: String): Boolean

    fun observeRepo(repo: String): LiveData<Repo>

    fun fetchRepos(user: String, type: RepoType): Boolean

    fun observeRepos(user: String, type: RepoType): LiveData<List<Repo>>
}
