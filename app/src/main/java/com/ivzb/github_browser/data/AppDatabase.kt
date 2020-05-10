package com.ivzb.github_browser.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ivzb.github_browser.data.repo.RepoFtsDao
import com.ivzb.github_browser.data.user.UserFtsDao
import com.ivzb.github_browser.model.repo.RepoFtsEntity
import com.ivzb.github_browser.model.user.UserFtsEntity

/**
 * The [Room] database for this app.
 */
@Database(
    entities = [
        UserFtsEntity::class,
        RepoFtsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(), DatabaseDataSource {

    companion object {

        private const val databaseName = "github_browser_db"

        fun buildDatabase(context: Context): AppDatabase {
            // Database is version 1 so destructive migration is enough for now.
            // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
            return Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
