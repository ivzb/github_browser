package com.ivzb.github_browser.domain.user

import androidx.lifecycle.LiveData
import com.ivzb.github_browser.data.user.UserRepository
import com.ivzb.github_browser.domain.MediatorUseCase
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.model.user.User
import javax.inject.Inject

open class ObserveUserUseCase @Inject constructor(
    private val repository: UserRepository
) : MediatorUseCase<String?, User?>() {

    private var user: LiveData<User?>? = null

    override fun execute(parameters: String?) {
        user?.let { source ->
            result.removeSource(source)
        }

        user = repository.observeUser(parameters).also { source ->
            result.addSource(source) { list ->
                result.postValue(Result.Success(list))
            }
        }
    }
}
