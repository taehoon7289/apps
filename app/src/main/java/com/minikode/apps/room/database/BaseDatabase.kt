package com.minikode.apps.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.minikode.apps.dao.AlarmDao
import com.minikode.apps.dao.DonationDao
import com.minikode.apps.dao.LikeDao
import com.minikode.apps.entity.AlarmEntity
import com.minikode.apps.entity.DonationEntity
import com.minikode.apps.entity.LikeEntity

@Database(entities = [AlarmEntity::class, LikeEntity::class, DonationEntity::class], version = 2)
abstract class BaseDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun likeDao(): LikeDao
    abstract fun donationDao(): DonationDao

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
                ).addMigrations(MIGRATION_1_2)
                    .build()

                this.instance = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `donation` (`donationNo` INTEGER PRIMARY KEY AUTOINCREMENT, `orderId` TEXT, `purchaseState` INTEGER, `purchaseTime` INTEGER, `purchaseToken` TEXT, `packageName` TEXT, `developerPayload` TEXT, `quantity` INTEGER, `signature` TEXT, `createDate` INTEGER, `products` TEXT);")
            }

        }

    }

}