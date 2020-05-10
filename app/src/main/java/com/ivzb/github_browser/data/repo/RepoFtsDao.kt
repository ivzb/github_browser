package com.ivzb.github_browser.data.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ivzb.github_browser.model.db.RepoFtsEntity

/**
 * The Data Access Object for the [RepoFtsEntity] class.
 */
@Dao
interface RepoFtsDao {

    @Query(
        """
        SELECT rowid, name, full_name, description, owner, is_fork, stars_count, watchers_count, forks_count, language
        FROM repoFts 
        WHERE owner = :user
        ORDER BY stars_count DESC
        """
    )
    fun observeOwn(user: String): LiveData<List<RepoFtsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOwn(users: List<RepoFtsEntity>)
}
