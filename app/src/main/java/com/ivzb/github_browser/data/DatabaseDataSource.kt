package com.ivzb.github_browser.data

import com.ivzb.github_browser.data.repo.RepoFtsDao
import com.ivzb.github_browser.data.repo_type.RepoTypeFtsDao
import com.ivzb.github_browser.data.user.UserFtsDao
import com.ivzb.github_browser.data.user_type.UserTypeFtsDao

interface DatabaseDataSource {

    fun userFtsDao(): UserFtsDao

    fun userTypeFtsDao(): UserTypeFtsDao

    fun repoFtsDao(): RepoFtsDao

    fun repoTypeFtsDao(): RepoTypeFtsDao
}
