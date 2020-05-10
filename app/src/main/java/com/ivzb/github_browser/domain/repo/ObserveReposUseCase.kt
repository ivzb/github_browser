package com.ivzb.github_browser.domain.repo

import androidx.lifecycle.LiveData
import com.ivzb.github_browser.data.repo.RepoRepository
import com.ivzb.github_browser.domain.MediatorUseCase
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.model.repo.Repo
import com.ivzb.github_browser.model.repo.RepoType
import javax.inject.Inject

open class ObserveReposUseCase @Inject constructor(
    private val repository: RepoRepository
) : MediatorUseCase<Pair<String, RepoType>, List<Repo>?>() {

    private var repos: LiveData<List<Repo>>? = null

    override fun execute(parameters: Pair<String, RepoType>) {
        val (user, type) = parameters

        repos?.let { source ->
            result.removeSource(source)
        }

        repos = repository.observeRepos(user, type).also { source ->
            result.addSource(source) { list ->
                result.postValue(Result.Success(list))
            }
        }
    }
}
