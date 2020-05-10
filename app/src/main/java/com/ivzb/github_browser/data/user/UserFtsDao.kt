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
        WHERE (user IS NULL AND type IS NULL AND :user IS NULL) OR (login = :user AND :user IS NOT NULL)
        LIMIT 1
        """
    )
    fun observe(user: String?): LiveData<UserFtsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserFtsEntity)

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
