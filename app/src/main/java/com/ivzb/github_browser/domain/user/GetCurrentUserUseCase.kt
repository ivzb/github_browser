package com.ivzb.github_browser.domain.user

import com.ivzb.github_browser.data.user.UserRepository
import com.ivzb.github_browser.domain.UseCase
import com.ivzb.github_browser.model.user.User
import javax.inject.Inject

open class GetCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository
) : UseCase<Unit, User?>() {

    override fun execute(parameters: Unit) =
        repository.getCurrentUser()
}
