package com.ivzb.github_browser.data

import com.ivzb.github_browser.data.repo.RepoFtsDao
import com.ivzb.github_browser.data.user.UserFtsDao

interface DatabaseDataSource {

    fun userFtsDao(): UserFtsDao

    fun repoFtsDao(): RepoFtsDao
}
