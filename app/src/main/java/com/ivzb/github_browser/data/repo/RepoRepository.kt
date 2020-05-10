package com.ivzb.github_browser.data.repo

import androidx.lifecycle.LiveData
import com.ivzb.github_browser.data.DatabaseDataSource
import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.model.repo.RepoType
import com.ivzb.github_browser.util.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to repo data for the presentation layer.
 */
@Singleton
open class RepoRepository @Inject constructor(
    private val remoteDataSource: RepoRemoteDataSource,
    private val database: DatabaseDataSource
) : RepoDataSource, RepoRemoteDataSource by remoteDataSource {

    override fun fetchRepo(repo: String) {
        remoteDataSource.getRepo(repo)?.let {
            it.asRepoFtsEntity().let { repo ->
                database.repoFtsDao().insert(repo)
            }
        }
    }

    override fun observeRepo(repo: String): LiveData<Repo> =
        database
            .repoFtsDao()
            .observe(repo)
            .map { it.asRepo() }

    override fun fetchRepos(user: String, type: RepoType) {
        remoteDataSource.getRepos(user, type)?.let { repos ->
            val repoFtsEntities = repos.map { it.asRepoFtsEntity(user, type.name) }
            database.repoFtsDao().insertAll(repoFtsEntities)
        }
    }

    override fun observeRepos(user: String, type: RepoType): LiveData<List<Repo>> =
        database
            .repoFtsDao()
            .observeAll(user, type.name)
            .map {
                it.toSet().map { repo -> repo.asRepo() }
            }
}

