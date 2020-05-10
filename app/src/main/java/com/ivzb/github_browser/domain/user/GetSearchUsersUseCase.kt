package com.ivzb.github_browser.domain.user

import com.ivzb.github_browser.data.user.UserRepository
import com.ivzb.github_browser.domain.UseCase
import com.ivzb.github_browser.model.ui.User
import javax.inject.Inject

open class GetSearchUsersUseCase @Inject constructor(
    private val repository: UserRepository
) : UseCase<String, List<User>?>() {

    override fun execute(parameters: String) =
        repository.getSearchUsers(parameters)
}
