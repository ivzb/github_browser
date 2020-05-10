package com.ivzb.github_browser.domain.repo

import androidx.lifecycle.LiveData
import com.ivzb.github_browser.data.repo.RepoRepository
import com.ivzb.github_browser.domain.MediatorUseCase
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.model.repo.Repo
import javax.inject.Inject

open class ObserveRepoUseCase @Inject constructor(
    private val repository: RepoRepository
) : MediatorUseCase<String, Repo?>() {

    private var repo: LiveData<Repo>? = null

    override fun execute(parameters: String) {
        repo?.let { source ->
            result.removeSource(source)
        }

        repo = repository.observeRepo(parameters).also { source ->
            result.addSource(source) { repo ->
                result.postValue(Result.Success(repo))
            }
        }
    }
}
