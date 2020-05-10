package com.ivzb.github_browser.model.repo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.ivzb.github_browser.model.repo.Repo

/**
 * This class represents [Repo] data for searching with FTS (full-text search).
 *
 * The [ColumnInfo] name is explicitly declared to allow flexibility for renaming the data class
 * properties without requiring changing the column name.
 */
@Entity(tableName = "repoFts")
@Fts4
data class RepoFtsEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "rowid")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "owner")
    val owner: String,

    @ColumnInfo(name = "is_fork")
    val isFork: Boolean,

    @ColumnInfo(name = "stars_count")
    val starsCount: Int,

    @ColumnInfo(name = "watchers_count")
    val watchersCount: Int,

    @ColumnInfo(name = "forks_count")
    val forksCount: Int,

    @ColumnInfo(name = "language")
    val language: String
) {

    fun asRepo() = Repo(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        description = this.description,
        owner = this.owner,
        isFork = this.isFork,
        starsCount = this.starsCount,
        watchersCount = this.watchersCount,
        forksCount = this.forksCount,
        language = this.language
    )
}
