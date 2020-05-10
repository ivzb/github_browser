package com.ivzb.github_browser.domain.user

import androidx.lifecycle.LiveData
import com.ivzb.github_browser.data.user.UserRepository
import com.ivzb.github_browser.domain.MediatorUseCase
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.model.user.UserType
import javax.inject.Inject

open class ObserveUsersUseCase @Inject constructor(
    private val repository: UserRepository
) : MediatorUseCase<Pair<String, UserType>, List<User>?>() {

    private var repos: LiveData<List<User>>? = null

    override fun execute(parameters: Pair<String, UserType>) {
        val (user, type) = parameters

        repos?.let { source ->
            result.removeSource(source)
        }

        repos = repository.observeUsers(user, type).also { source ->
            result.addSource(source) { list ->
                result.postValue(Result.Success(list))
            }
        }
    }
}
