package com.ivzb.github_browser.data.repo

import androidx.lifecycle.LiveData
import com.ivzb.github_browser.data.AppDatabase
import com.ivzb.github_browser.model.ui.Repo
import com.ivzb.github_browser.util.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to repo data for the presentation layer.
 */
@Singleton
open class RepoRepository @Inject constructor(
    private val dataSource: RepoDataSource,
    private val appDatabase: AppDatabase
) : RepoDataSource by dataSource {

    fun fetchOwnRepos(user: String) {
        dataSource.getOwnRepos(user)?.let { repos ->
            val repoFtsEntities = repos.map { repo -> repo.asRepoFtsEntity() }
            appDatabase.repoFtsDao().insertOwn(repoFtsEntities)
        }
    }

    fun observeOwnRepos(user: String): LiveData<List<Repo>> =
        appDatabase
            .repoFtsDao()
            .observeOwn(user)
            .map {
                it.toSet().map { repo -> repo.asRepo() }
            }
}
