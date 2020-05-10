package com.ivzb.github_browser.domain.repo

import com.ivzb.github_browser.data.repo.RepoRepository
import com.ivzb.github_browser.domain.UseCase
import com.ivzb.github_browser.model.repo.RepoType
import javax.inject.Inject

open class FetchReposUseCase @Inject constructor(
    private val repository: RepoRepository
) : UseCase<Pair<String, RepoType>, Boolean>() {

    override fun execute(parameters: Pair<String, RepoType>): Boolean {
        val (user, type) = parameters

        return repository.fetchRepos(user, type)
    }
}
