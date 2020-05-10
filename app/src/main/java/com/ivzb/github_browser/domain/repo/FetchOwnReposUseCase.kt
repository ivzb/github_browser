package com.ivzb.github_browser.domain.repo

import com.ivzb.github_browser.data.repo.RepoRepository
import com.ivzb.github_browser.domain.UseCase
import javax.inject.Inject

open class FetchOwnReposUseCase @Inject constructor(
    private val repository: RepoRepository
) : UseCase<String, Unit>() {

    override fun execute(parameters: String) =
        repository.fetchOwnRepos(parameters)
}
