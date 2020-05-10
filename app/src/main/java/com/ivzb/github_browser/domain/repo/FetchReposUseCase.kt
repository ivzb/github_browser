package com.ivzb.github_browser.domain.repo

import com.ivzb.github_browser.data.repo.RepoRepository
import com.ivzb.github_browser.domain.UseCase
import com.ivzb.github_browser.model.repo.RepoType
import javax.inject.Inject

open class FetchReposUseCase @Inject constructor(
    private val repository: RepoRepository
) : UseCase<Pair<String, RepoType>, Unit>() {

    override fun execute(parameters: Pair<String, RepoType>) {
        val (user, type) = parameters

        repository.fetchRepos(user, type)
    }
}
