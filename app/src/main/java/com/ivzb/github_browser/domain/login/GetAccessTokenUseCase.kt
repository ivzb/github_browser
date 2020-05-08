package com.ivzb.github_browser.domain.login

import com.ivzb.github_browser.data.login.LoginRepository
import com.ivzb.github_browser.domain.UseCase
import com.ivzb.github_browser.model.ui.AccessToken
import javax.inject.Inject

open class GetAccessTokenUseCase @Inject constructor(
    private val repository: LoginRepository
) : UseCase<AccessTokenParameters, AccessToken?>() {

    override fun execute(parameters: AccessTokenParameters) =
        repository.getAccessToken(parameters.clientId, parameters.clientSecret, parameters.code)
}

data class AccessTokenParameters(
    val clientId: String,
    val clientSecret: String,
    val code: String
)
