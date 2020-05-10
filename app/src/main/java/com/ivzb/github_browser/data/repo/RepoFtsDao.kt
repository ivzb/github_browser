package com.ivzb.github_browser.data.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ivzb.github_browser.model.db.RepoFtsEntity

/**
 * The Data Access Object for the [RepoFtsEntity] class.
 */
@Dao
interface UserFtsDao {

    @Query(
        """
        SELECT rowid, name, full_name, description, is_fork, stars_count, watchers_count, forks_count, language
        FROM repoFts 
        ORDER BY stars_count DESC
        """
    )
    fun observeAll(): LiveData<List<RepoFtsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<RepoFtsEntity>)
}
