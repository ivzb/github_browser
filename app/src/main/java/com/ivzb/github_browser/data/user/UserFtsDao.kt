package com.ivzb.github_browser.data.user

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ivzb.github_browser.model.user.UserFtsEntity

/**
 * The Data Access Object for the [UserFtsEntity] class.
 */
@Dao
interface UserFtsDao {

    @Query(
        """
        SELECT rowid, user, type, login, name, avatar_url, repos, followers, following, contributions 
        FROM userFts 
        WHERE user = :user and type = :type
        ORDER BY followers DESC
        """
    )
    fun observeAll(user: String, type: String): LiveData<List<UserFtsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<UserFtsEntity>)
}
