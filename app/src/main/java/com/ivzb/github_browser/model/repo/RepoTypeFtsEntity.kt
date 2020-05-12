package com.ivzb.github_browser.model.repo

import androidx.room.*

@Entity(tableName = "repoTypeFts", primaryKeys = ["repoid", "typeid", "user"])
data class RepoTypeFtsEntity(

    @ColumnInfo(name = "repoid")
    val repoId: Long,

    @ColumnInfo(name = "typeid")
    val typeId: Int,

    @ColumnInfo(name = "user")
    val user: String
)
