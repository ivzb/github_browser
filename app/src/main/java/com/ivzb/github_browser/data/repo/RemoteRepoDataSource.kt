package com.ivzb.github_browser.data.repo

import com.ivzb.github_browser.model.ui.Repo
import com.ivzb.github_browser.util.NetworkUtils
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteRepoDataSource @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val retrofit: Retrofit
) : RepoDataSource {

    override fun getOwnRepos(user: String): List<Repo>? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<RepoAPI>(RepoAPI::class.java).getOwnRepos(user).execute()

        return response.body()?.map { it.asRepo() }
    }

    override fun getStarredRepos(user: String): List<Repo>? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<RepoAPI>(RepoAPI::class.java).getStarredRepos(user).execute()

        return response.body()?.map { it.asRepo() }
    }

    override fun getRepo(repo: String): Repo? {
        if (!networkUtils.hasNetworkConnection()) {
            return null
        }

        val response = retrofit.create<RepoAPI>(RepoAPI::class.java).getRepo(repo).execute()

        return response.body()?.asRepo()
    }
}
