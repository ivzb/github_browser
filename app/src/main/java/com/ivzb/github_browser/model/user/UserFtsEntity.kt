package com.ivzb.github_browser.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.ivzb.github_browser.model.user.User

/**
 * This class represents [User] data for searching with FTS (full-text search).
 *
 * The [ColumnInfo] name is explicitly declared to allow flexibility for renaming the data class
 * properties without requiring changing the column name.
 */
@Entity(tableName = "userFts")
data class UserFtsEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "rowid")
    val id: Long,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,

    @ColumnInfo(name = "repos")
    val repos: Int,

    @ColumnInfo(name = "followers")
    val followers: Int,

    @ColumnInfo(name = "following")
    val following: Int,

    @ColumnInfo(name = "contributions")
    val contributions: Int
) {

    fun asUser() = User(
        id = this.id,
        login = this.login,
        name = this.name,
        avatarUrl = this.avatarUrl,
        repos = this.repos,
        followers = this.followers,
        following = this.following,
        contributions = this.contributions
    )
}
