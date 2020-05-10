package com.ivzb.github_browser.domain.repo

import com.ivzb.github_browser.data.repo.RepoRepository
import com.ivzb.github_browser.domain.MediatorUseCase
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.model.ui.Repo
import javax.inject.Inject

open class ObserveOwnReposUseCase @Inject constructor(
    private val repository: RepoRepository
) : MediatorUseCase<String, List<Repo>?>() {

    override fun execute(parameters: String) =
        result.addSource(repository.observeOwnRepos(parameters)) {
            result.postValue(Result.Success(it))
        }
}
