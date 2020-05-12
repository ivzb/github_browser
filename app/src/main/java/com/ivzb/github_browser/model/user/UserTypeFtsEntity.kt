package com.ivzb.github_browser.model.user

import androidx.room.*

@Entity(tableName = "userTypeFts", primaryKeys = ["userid", "typeid", "user"])
data class UserTypeFtsEntity(

    @ColumnInfo(name = "userid")
    val userId: Long,

    @ColumnInfo(name = "typeid")
    val typeId: Int,

    @ColumnInfo(name = "user")
    val user: String
)
