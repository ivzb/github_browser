package com.ivzb.github_browser.data.repo

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to repo data for the presentation layer.
 */
@Singleton
open class RepoRepository @Inject constructor(
    private val dataSource: RepoDataSource
): RepoDataSource by dataSource
