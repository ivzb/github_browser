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
        INNER JOIN userTypeFts ut ON rowid = ut.userid
        WHERE ut.user = :user OR login = :user
        LIMIT 1
        """
    )
    fun observe(user: String?): LiveData<UserFtsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserFtsEntity)

    @Query(
        """
        SELECT rowid, login, name, avatar_url, repos, followers, following, contributions 
        FROM userFts 
        INNER JOIN userTypeFts ut ON rowid = ut.userid
        WHERE ut.user = :user and ut.typeid = :typeId
        ORDER BY followers DESC
        """
    )
    fun observeAll(user: String, typeId: Int): LiveData<List<UserFtsEntity>>
}
