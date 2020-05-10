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
        SELECT rowid, login, name, avatar_url, repos, followers, following, contributions 
        FROM userFts 
        ORDER BY followers DESC
        """
    )
    fun observeAll(): LiveData<List<UserFtsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<UserFtsEntity>)
}
