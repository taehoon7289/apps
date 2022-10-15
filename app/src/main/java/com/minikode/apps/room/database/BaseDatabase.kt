package com.minikode.apps.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.minikode.apps.dao.AlarmDao
import com.minikode.apps.dao.LikeDao
import com.minikode.apps.entity.AlarmEntity
import com.minikode.apps.entity.LikeEntity

@Database(entities = [AlarmEntity::class, LikeEntity::class], version = 1)
abstract class BaseDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun likeDao(): LikeDao

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