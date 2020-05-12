package com.ivzb.github_browser.data.user

import androidx.lifecycle.LiveData
import com.ivzb.github_browser.data.DatabaseDataSource
import com.ivzb.github_browser.model.user.User
import com.ivzb.github_browser.model.user.UserType
import com.ivzb.github_browser.model.user.UserTypeFtsEntity
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

    override fun fetchUser(user: String?): Boolean {
        remoteDataSource.getUser(user)?.let {
            it.asUserFtsEntity().let { entity ->
                database.userFtsDao().insert(entity)

                if (user == null) {
                    database.userTypeFtsDao().insert(UserTypeFtsEntity(it.id, CURRENT_USER_TYPE_ID, CURRENT_USER_NAME))
                }
            }

            return true
        }

        return false
    }

    override fun observeUser(user: String?): LiveData<User?> =
        database
            .userFtsDao()
            .observe(user ?: CURRENT_USER_NAME)
            .map { it?.asUser() }

    override fun fetchUsers(user: String, type: UserType): Boolean {
        remoteDataSource.getUsers(user, type)?.let { users ->
            users
                .map { it.asUserFtsEntity() }
                .forEach {
                    database.userFtsDao().insert(it)
                    database.userTypeFtsDao().insert(UserTypeFtsEntity(it.id, type.ordinal, user))
                }

            return true
        }

        return false
    }

    override fun observeUsers(user: String, type: UserType): LiveData<List<User>> =
        database
            .userFtsDao()
            .observeAll(user, type.ordinal)
            .map {
                it.toSet().map { user -> user.asUser() }
            }

    companion object {
        private const val CURRENT_USER_NAME = ""
        private const val CURRENT_USER_TYPE_ID = -1
    }
}
