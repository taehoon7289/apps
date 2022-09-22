package com.example.app_drawer.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app_drawer.dao.AlarmDao
import com.example.app_drawer.entity.AlarmEntity

@Database(entities = [AlarmEntity::class], version = 1)
abstract class BaseDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var instance: BaseDatabase? = null

        fun getDatabase(
            context: Context
        ): BaseDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatabase::class.java,
                    "base_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                this.instance = instance
                instance
            }
        }
    }
}