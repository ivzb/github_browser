package com.ivzb.github_browser.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.github_browser.domain.Event
import com.ivzb.github_browser.domain.Result
import com.ivzb.github_browser.domain.successOr
import com.ivzb.github_browser.domain.user.GetContributorsUseCase
import com.ivzb.github_browser.domain.user.GetFollowersUseCase
import com.ivzb.github_browser.domain.user.GetFollowingUseCase
import com.ivzb.github_browser.domain.user.GetSearchUsersUseCase
import com.ivzb.github_browser.model.ui.User
import com.ivzb.github_browser.ui.Empty
import com.ivzb.github_browser.ui.NoConnection
import com.ivzb.github_browser.util.checkAllMatched
import com.ivzb.github_browser.util.map
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    private val getFollowingUseCase: GetFollowingUseCase,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getContributorsUseCase: GetContributorsUseCase,
    private val getSearchUsersUseCase: GetSearchUsersUseCase
) : ViewModel() {

    val loading = MutableLiveData<Event<Boolean>>()
    val click = MutableLiveData<Event<User>>()
    val users: LiveData<List<Any>>
    val searchQuery = MutableLiveData<String>()

    private val getUsersResult = MutableLiveData<Result<List<User>?>>()

    init {
        users = getUsersResult.map {
            loading.postValue(Event(false))

            val result = it.successOr(listOf(NoConnection)) ?: listOf(Empty)

            when {
                result.isEmpty() -> listOf(Empty)
                else -> result
            }
        }
    }

    fun getUsers(user: String, type: UsersType) {
        loading.postValue(Event(true))

        when (type) {
            UsersType.Following -> getFollowingUseCase(user, getUsersResult)
            UsersType.Followers -> getFollowersUseCase(user, getUsersResult)
            UsersType.Contributors -> getContributorsUseCase(user, getUsersResult)
            UsersType.Search -> getSearchUsersUseCase(user, getUsersResult)
        }.checkAllMatched
    }

    fun click(user: User) {
        click.postValue(Event(user))
    }

    fun search(query: String?) {
        searchQuery.postValue(query ?: "")
    }
}
