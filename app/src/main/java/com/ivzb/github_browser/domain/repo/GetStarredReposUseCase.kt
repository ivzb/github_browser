package com.ivzb.github_browser.domain.repo

import com.ivzb.github_browser.data.repo.RepoRepository
import com.ivzb.github_browser.domain.UseCase
import com.ivzb.github_browser.model.repo.Repo
import javax.inject.Inject

open class GetStarredReposUseCase @Inject constructor(
    private val repository: RepoRepository
) : UseCase<String, List<Repo>?>() {

    override fun execute(parameters: String) =
        repository.getStarredRepos(parameters)
}
