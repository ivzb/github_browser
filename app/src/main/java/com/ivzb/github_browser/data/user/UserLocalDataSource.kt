package com.ivzb.github_browser.data.user

import androidx.lifecycle.LiveData
import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.model.user.UserType

interface UserLocalDataSource {

    fun fetchUser(user: String?)

    fun observeUser(user: String?): LiveData<User?>

    fun fetchUsers(user: String, type: UserType)

    fun observeUsers(user: String, type: UserType): LiveData<List<User>>
}
