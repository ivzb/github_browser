package com.ivzb.github_browser.data.user

import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.model.user.UserType

interface UserRemoteDataSource {

    fun getUser(user: String?): User?

    fun getUsers(user: String, type: UserType): List<User>?
}
