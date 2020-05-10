package com.ivzb.github_browser.domain.user

import com.ivzb.github_browser.data.user.UserRepository
import com.ivzb.github_browser.domain.UseCase
import com.ivzb.github_browser.model.user.UserType
import javax.inject.Inject

open class FetchUsersUseCase @Inject constructor(
    private val repository: UserRepository
) : UseCase<Pair<String, UserType>, Unit>() {

    override fun execute(parameters: Pair<String, UserType>) {
        val (user, type) = parameters

        repository.fetchUsers(user, type)
    }
}
