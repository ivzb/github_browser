package com.ivzb.github_browser.data.repo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ivzb.github_browser.model.repo.RepoFtsEntity
import com.ivzb.github_browser.model.repo.RepoType

/**
 * The Data Access Object for the [RepoFtsEntity] class.
 */
@Dao
interface RepoFtsDao {

    @Query(
        """
        SELECT rowid, user, type, name, full_name, description, owner, is_fork, stars_count, watchers_count, forks_count, language
        FROM repoFts 
        WHERE full_name = :repo
        LIMIT 1
        """
    )
    fun observe(repo: String): LiveData<RepoFtsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: RepoFtsEntity)

    @Query(
        """
        SELECT rowid, user, type, name, full_name, description, owner, is_fork, stars_count, watchers_count, forks_count, language
        FROM repoFts 
        WHERE user = :user AND type = :type
        ORDER BY stars_count DESC
        """
    )
    fun observeAll(user: String, type: String): LiveData<List<RepoFtsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<RepoFtsEntity>)
}
