package com.ivzb.github_browser.data.user_type

import androidx.room.*
import com.ivzb.github_browser.model.user.UserTypeFtsEntity

/**
 * The Data Access Object for the [UserTypeFtsEntity] class.
 */
@Dao
interface UserTypeFtsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userType: UserTypeFtsEntity)
}
