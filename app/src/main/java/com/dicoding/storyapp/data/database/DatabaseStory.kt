package com.dicoding.storyapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.storyapp.data.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 2,
    exportSchema = true
)
abstract class DatabaseStory : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseStory? = null

        @JvmStatic
        fun getDatabase(context: Context): DatabaseStory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseStory::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}