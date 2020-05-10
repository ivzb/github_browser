package com.ivzb.github_browser.domain.user

import com.ivzb.github_browser.data.user.UserRepository
import com.ivzb.github_browser.domain.UseCase
import javax.inject.Inject

open class FetchUserUseCase @Inject constructor(
    private val repository: UserRepository
) : UseCase<String?, Unit>() {

    override fun execute(parameters: String?) {
        repository.fetchUser(parameters)
    }
}
