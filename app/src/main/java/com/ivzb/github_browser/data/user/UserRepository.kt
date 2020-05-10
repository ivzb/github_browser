package com.ivzb.github_browser.data.user

import androidx.lifecycle.LiveData
import com.ivzb.github_browser.data.DatabaseDataSource
import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.model.user.UserType
import com.ivzb.github_browser.util.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to user data for the presentation layer.
 */
@Singleton
open class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val database: DatabaseDataSource
) : UserDataSource, UserRemoteDataSource by remoteDataSource {

    override fun fetchUsers(user: String, type: UserType) {
        remoteDataSource.getUsers(user, type)?.let { users ->
            val userFtsEntities = users.map { it.asUserFtsEntity(user, type.name) }
            database.userFtsDao().insertAll(userFtsEntities)
        }
    }

    override fun observeUsers(user: String, type: UserType): LiveData<List<User>> =
        database
            .userFtsDao()
            .observeAll(user, type.name)
            .map {
                it.toSet().map { user -> user.asUser() }
            }
}
