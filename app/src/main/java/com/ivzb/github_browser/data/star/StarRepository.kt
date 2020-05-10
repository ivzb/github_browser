package com.ivzb.github_browser.data.star

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to star data for the presentation layer.
 */
@Singleton
open class StarRepository @Inject constructor(
    private val remoteDataSource: StarRemoteDataSource
) : StarDataSource, StarRemoteDataSource by remoteDataSource
