package com.ivzb.github_browser.data.user

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to user data for the presentation layer.
 */
@Singleton
open class UserRepository @Inject constructor(
    private val dataSource: UserDataSource
): UserDataSource by dataSource
