package com.ivzb.github_browser.data.repo_type

import androidx.room.*
import com.ivzb.github_browser.model.repo.RepoTypeFtsEntity

/**
 * The Data Access Object for the [RepoTypeFtsEntity] class.
 */
@Dao
interface RepoTypeFtsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repoType: RepoTypeFtsEntity)
}
