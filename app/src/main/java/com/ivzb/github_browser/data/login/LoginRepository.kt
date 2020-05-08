package com.ivzb.github_browser.data.login

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to login data for the presentation layer.
 */
@Singleton
open class LoginRepository @Inject constructor(
    private val dataSource: LoginDataSource
): LoginDataSource by dataSource
