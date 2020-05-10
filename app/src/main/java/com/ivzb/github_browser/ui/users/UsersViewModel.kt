package com.ivzb.github_browser.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.domain.user.*
import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.model.user.UserType
import com.ivzb.github_browser.ui.Empty
import com.ivzb.github_browser.ui.NoConnection
import com.ivzb.github_browser.util.checkAllMatched
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    private val observeUsersUseCase: ObserveUsersUseCase,
    private val fetchUsersUseCase: FetchUsersUseCase
) : ViewModel() {

    val loading: MutableLiveData<Event<Boolean>>
    val click = MutableLiveData<Event<User>>()
    val users: LiveData<Event<List<Any>>>
    val searchQuery = MutableLiveData<Event<String>>()

    private val fetchUsersResult = MutableLiveData<Result<Boolean>>()

    init {
        users = observeUsersUseCase.observe().map {
            val result = it.successOr(listOf(NoConnection)) ?: listOf(Empty)

            Event(
                when {
                    result.isEmpty() -> listOf(Empty)
                    else -> result
                }
            )
        }

        loading = fetchUsersResult.map {
            Event(false)
        } as MutableLiveData<Event<Boolean>>
    }

    fun getUsers(user: String, type: UserType) {
        loading.postValue(Event(true))

        Pair(user, type).run {
            observeUsersUseCase.execute(this)
            fetchUsersUseCase(this, fetchUsersResult)
        }
    }

    fun click(user: User) =
        click.postValue(Event(user))

    fun search(query: String?) =
        searchQuery.postValue(Event(query ?: ""))
}
