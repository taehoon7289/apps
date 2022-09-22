package com.example.app_drawer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.app_drawer.entity.AlarmEntity

@Dao
interface AlarmDao {
    @Insert
    fun insert(alarmEntity: AlarmEntity)

    @Delete
    fun delete(alarmEntity: AlarmEntity)

    @Delete
    fun deleteById(requestCode: Int)

}