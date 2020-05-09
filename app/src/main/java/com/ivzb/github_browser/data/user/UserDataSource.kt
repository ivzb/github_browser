package com.ivzb.github_browser.data.user

import com.ivzb.github_browser.model.ui.User

interface UserDataSource {

    fun getCurrentUser(): User?
}
