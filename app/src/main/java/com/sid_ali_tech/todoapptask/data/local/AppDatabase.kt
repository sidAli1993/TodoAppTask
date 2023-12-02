package com.sid_ali_tech.todoapptask.data.local

import android.content.Context
import android.graphics.ColorSpace.Model
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sid_ali_tech.todoapptask.data.local.dao.TasksDao
import com.sid_ali_tech.todoapptask.domain.model.Todo

@androidx.room.Database(entities = [Todo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TasksDao?

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "artbotic.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}